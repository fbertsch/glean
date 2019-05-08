use std::fs::{create_dir_all, File};
use std::io::Write;
use std::path::{Path, PathBuf};

use log::info;
use serde_json::{json, Value as JsonValue};

use crate::storage::StorageManager;

pub struct PingMaker;

fn merge(a: &mut JsonValue, b: &JsonValue) {
    match (a, b) {
        (&mut JsonValue::Object(ref mut a), &JsonValue::Object(ref b)) => {
            for (k, v) in b {
                merge(a.entry(k.clone()).or_insert(JsonValue::Null), v);
            }
        }
        (a, b) => {
            *a = b.clone();
        }
    }
}

impl Default for PingMaker {
    fn default() -> Self {
        Self::new()
    }
}

impl PingMaker {
    pub fn new() -> Self {
        Self
    }

    fn get_ping_seq(&self, _storage: &str) -> usize {
        1
    }

    fn get_ping_info(&self, storage: &str) -> JsonValue {
        json!({
            "ping_type": storage,
            "seq": self.get_ping_seq(storage),
        })
    }

    fn get_client_info(&self) -> JsonValue {
        let client_info = StorageManager.snapshot_as_json("glean_client_info", true);
        let mut map = json!({});

        // Flatten the whole thing.
        let client_info_obj = client_info.as_object().unwrap(); // safe, snapshot always returns an object.
        for (_key, value) in client_info_obj {
            merge(&mut map, value);
        }

        json!(map)
    }

    pub fn collect(&self, storage: &str) -> JsonValue {
        info!("Collecting {}", storage);

        let metrics_data = StorageManager.snapshot_as_json(storage, true);

        let ping_info = self.get_ping_info(storage);
        let client_info = self.get_client_info();

        json!({
            "ping_info": ping_info,
            "client_info": client_info,
            "metrics": metrics_data
        })
    }

    pub fn collect_string(&self, storage: &str) -> String {
        let ping = self.collect(storage);
        ::serde_json::to_string_pretty(&ping).unwrap()
    }

    fn get_pings_dir(&self, data_path: &Path) -> std::io::Result<PathBuf> {
        let pings_dir = data_path.join("pings");
        create_dir_all(&pings_dir)?;
        Ok(pings_dir)
    }

    /// Store a ping to disk in the pings directory.
    pub fn store_ping(
        &self,
        doc_id: &str,
        data_path: &Path,
        url_path: &str,
        ping_content: &str,
    ) -> std::io::Result<()> {
        let pings_dir = self.get_pings_dir(data_path)?;

        // Write to a temporary location and then move when done,
        // for transactional writes.
        let temp_ping_path = std::env::temp_dir().join(doc_id);
        let ping_path = pings_dir.join(doc_id);

        {
            let mut file = File::create(&temp_ping_path)?;
            file.write_all(url_path.as_bytes())?;
            file.write_all(b"\n")?;
            file.write_all(ping_content.as_bytes())?;
        }

        std::fs::rename(temp_ping_path, ping_path)?;

        Ok(())
    }
}