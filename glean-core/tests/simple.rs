use glean_core::metrics::*;
use glean_core::{storage, CommonMetricData, Glean, Lifetime};
use lazy_static::lazy_static;
use std::path::PathBuf;
use tempfile::Builder;

lazy_static! {
    pub static ref GLOBAL_METRIC: BooleanMetric = BooleanMetric::new(CommonMetricData {
        name: "global_metric".into(),
        category: "global".into(),
        send_in_pings: vec!["core".into(), "metrics".into()],
        lifetime: Lifetime::Ping,
        disabled: false,
    });
    pub static ref GLOBAL_TMP: String = {
        let root = Builder::new().prefix("simple-db").tempdir().unwrap();
        root.path().display().to_string()
    };
}

const GLOBAL_APPLICATION_ID: &str = "org.mozilla.glean.test.app";

#[test]
fn it_works() {
    Glean::singleton().initialize(&*GLOBAL_TMP, &GLOBAL_APPLICATION_ID);
    assert!(Glean::singleton().is_initialized());
}

#[test]
fn can_set_metrics() {
    Glean::singleton().initialize(&*GLOBAL_TMP, &GLOBAL_APPLICATION_ID);

    let local_metric: StringMetric = StringMetric::new(CommonMetricData {
        name: "local_metric".into(),
        category: "local".into(),
        send_in_pings: vec!["core".into()],
        ..Default::default()
    });

    let call_counter: CounterMetric = CounterMetric::new(CommonMetricData {
        name: "calls".into(),
        category: "local".into(),
        send_in_pings: vec!["core".into(), "metrics".into()],
        ..Default::default()
    });

    GLOBAL_METRIC.set(true);
    local_metric.set("I can set this");
    call_counter.add(1);
}

#[test]
fn can_snapshot() {
    Glean::singleton().initialize(&*GLOBAL_TMP, &GLOBAL_APPLICATION_ID);

    let local_metric: StringMetric = StringMetric::new(CommonMetricData {
        name: "can_snapshot_local_metric".into(),
        category: "local".into(),
        send_in_pings: vec!["core".into()],
        ..Default::default()
    });

    local_metric.set("snapshot 42");

    let snapshot = storage::StorageManager.snapshot("core", false);
    assert!(snapshot.contains(r#""local.can_snapshot_local_metric": "snapshot 42""#));
}

#[test]
fn snapshot_can_clear_ping_store() {
    Glean::singleton().initialize(&*GLOBAL_TMP, &GLOBAL_APPLICATION_ID);

    let local_metric: StringMetric = StringMetric::new(CommonMetricData {
        name: "clear_snapshot_local_metric".into(),
        category: "local".into(),
        send_in_pings: vec!["core".into()],
        ..Default::default()
    });

    local_metric.set("snapshot 43");

    let snapshot = storage::StorageManager.snapshot("core", true);
    assert!(snapshot.contains(r#""local.clear_snapshot_local_metric": "snapshot 43""#));

    let snapshot = storage::StorageManager.snapshot("core", true);
    assert!(!snapshot.contains(r#""local.clear_snapshot_local_metric": "snapshot 43""#));
}

#[test]
fn clear_is_store_specific() {
    Glean::singleton().initialize(&*GLOBAL_TMP, &GLOBAL_APPLICATION_ID);

    let local_metric: StringMetric = StringMetric::new(CommonMetricData {
        name: "store_specific".into(),
        category: "local".into(),
        send_in_pings: vec!["core".into(), "baseline".into()],
        ..Default::default()
    });

    local_metric.set("snapshot 44");

    // Snapshot 1: Clear core.
    let core_snapshot = storage::StorageManager.snapshot("core", true);
    let baseline_snapshot = storage::StorageManager.snapshot("baseline", false);

    assert!(core_snapshot.contains(r#""local.store_specific": "snapshot 44""#));
    assert!(baseline_snapshot.contains(r#""local.store_specific": "snapshot 44""#));

    // Snapshot 2: Only baseline should contain the data.
    let core_snapshot = storage::StorageManager.snapshot("core", true);
    let baseline_snapshot = storage::StorageManager.snapshot("baseline", false);

    assert!(!core_snapshot.contains(r#""local.store_specific": "snapshot 44""#));
    assert!(baseline_snapshot.contains(r#""local.store_specific": "snapshot 44""#));
}

lazy_static! {
    pub static ref THREADSAFE_METRIC: CounterMetric = CounterMetric::new(CommonMetricData {
        name: "threadsafe".into(),
        category: "global".into(),
        send_in_pings: vec!["core".into(), "metrics".into()],
        lifetime: Lifetime::Ping,
        disabled: false,
    });
}

#[test]
fn thread_safety() {
    use std::sync::{Arc, Barrier};
    use std::thread;

    Glean::singleton().initialize(&*GLOBAL_TMP, &GLOBAL_APPLICATION_ID);

    let barrier = Arc::new(Barrier::new(2));
    let c = barrier.clone();
    let child = thread::spawn(move || {
        THREADSAFE_METRIC.add(1);
        c.wait();
        THREADSAFE_METRIC.add(1);
    });

    THREADSAFE_METRIC.add(1);
    barrier.wait();
    THREADSAFE_METRIC.add(1);

    child.join().unwrap();

    let snapshot = storage::StorageManager.snapshot("core", true);
    assert!(snapshot.contains(r#""global.threadsafe": 4"#));
}

#[test]
fn transformation_works() {
    Glean::singleton().initialize(&*GLOBAL_TMP, &GLOBAL_APPLICATION_ID);

    let counter: CounterMetric = CounterMetric::new(CommonMetricData {
        name: "transformation".into(),
        category: "local".into(),
        send_in_pings: vec!["core".into(), "metrics".into()],
        ..Default::default()
    });

    counter.add(2);
    let core_snapshot = storage::StorageManager.snapshot("core", true);
    let metrics_snapshot = storage::StorageManager.snapshot("metrics", false);
    assert!(
        core_snapshot.contains(r#""local.transformation": 2"#),
        format!("core snapshot 1: {}", core_snapshot)
    );
    assert!(
        metrics_snapshot.contains(r#""local.transformation": 2"#),
        format!("metrics snapshot 1: {}", metrics_snapshot)
    );

    counter.add(2);
    let core_snapshot = storage::StorageManager.snapshot("core", true);
    let metrics_snapshot = storage::StorageManager.snapshot("metrics", false);
    assert!(
        core_snapshot.contains(r#""local.transformation": 2"#),
        format!("core snapshot 2: {}", core_snapshot)
    );
    assert!(
        metrics_snapshot.contains(r#""local.transformation": 4"#),
        format!("metrics snapshot 2: {}", metrics_snapshot)
    );
}

#[test]
fn uuid() {
    Glean::singleton().initialize(&*GLOBAL_TMP, &GLOBAL_APPLICATION_ID);

    let uuid: UuidMetric = UuidMetric::new(CommonMetricData {
        name: "uuid".into(),
        category: "local".into(),
        send_in_pings: vec!["core".into()],
        ..Default::default()
    });

    uuid.generate();
    let snapshot = storage::StorageManager.snapshot("core", false);
    assert!(
        snapshot.contains(r#""local.uuid": ""#),
        format!("Snapshot 1: {}", snapshot)
    );

    uuid.generate();
    let snapshot = storage::StorageManager.snapshot("core", false);
    assert!(
        snapshot.contains(r#""local.uuid": ""#),
        format!("Snapshot 2: {}", snapshot)
    );
}

#[test]
fn list() {
    Glean::singleton().initialize(&*GLOBAL_TMP, &GLOBAL_APPLICATION_ID);

    let list: StringListMetric = StringListMetric::new(CommonMetricData {
        name: "list".into(),
        category: "local".into(),
        send_in_pings: vec!["core".into()],
        ..Default::default()
    });

    list.add("first");
    let snapshot = storage::StorageManager.snapshot("core", false);
    assert!(snapshot.contains(r#""local.list": ["#));
    assert!(snapshot.contains(r#""first""#));

    list.add("second");
    let snapshot = storage::StorageManager.snapshot("core", false);
    assert!(snapshot.contains(r#""local.list": ["#));
    assert!(snapshot.contains(r#""first""#));
    assert!(snapshot.contains(r#""second""#));

    list.set(vec!["third".into()]);
    let snapshot = storage::StorageManager.snapshot("core", false);
    assert!(snapshot.contains(r#""local.list": ["#));
    assert!(!snapshot.contains(r#""first""#));
    assert!(!snapshot.contains(r#""second""#));
    assert!(snapshot.contains(r#""third""#));
}

#[test]
fn write_ping_to_disk() {
    let temp: String = {
        let root = Builder::new().prefix("simple-db").tempdir().unwrap();
        root.path().display().to_string()
    };

    Glean::singleton().initialize(&*temp, &GLOBAL_APPLICATION_ID);

    GLOBAL_METRIC.set(false);

    Glean::singleton().send_ping("metrics").unwrap();

    let path = PathBuf::from(temp.as_str()).join("pings");

    let mut count = 0;
    for entry in std::fs::read_dir(path).unwrap() {
        assert!(entry.unwrap().path().is_file());
        count += 1;
    }
    assert!(count == 1);
}