// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

use std::os::raw::c_char;

use ffi_support::FfiStr;

use crate::{
    define_metric, from_raw_int64_array, handlemap_ext::HandleMapExtension, RawInt64Array, GLEAN,
};

define_metric!(MemoryDistributionMetric => MEMORY_DISTRIBUTION_METRICS {
    new           -> glean_new_memory_distribution_metric(memory_unit: i32),
    test_get_num_recorded_errors -> glean_memory_distribution_test_get_num_recorded_errors,
    destroy       -> glean_destroy_memory_distribution_metric,
    accumulate    -> glean_memory_distribution_accumulate(sample: u64),
});

#[no_mangle]
pub extern "C" fn glean_memory_distribution_accumulate_samples(
    glean_handle: u64,
    metric_id: u64,
    raw_samples: RawInt64Array,
    num_samples: i32,
) {
    GLEAN.call_infallible(glean_handle, |glean| {
        MEMORY_DISTRIBUTION_METRICS.call_infallible_mut(metric_id, |metric| {
            // The Kotlin code is sending Long(s), which are 64 bits, as there's
            // currently no stable UInt type. The positive part of [Int] would not
            // be enough to represent the values coming in:.
            // Here Long(s) are handled as i64 and then casted in `accumulate_samples_signed`
            // to u32.
            let samples = from_raw_int64_array(raw_samples, num_samples);
            metric.accumulate_samples_signed(glean, samples);
        })
    })
}

#[no_mangle]
pub extern "C" fn glean_memory_distribution_test_has_value(
    glean_handle: u64,
    metric_id: u64,
    storage_name: FfiStr,
) -> u8 {
    GLEAN.call_infallible(glean_handle, |glean| {
        MEMORY_DISTRIBUTION_METRICS.call_infallible(metric_id, |metric| {
            metric
                .test_get_value(glean, storage_name.as_str())
                .is_some()
        })
    })
}

#[no_mangle]
pub extern "C" fn glean_memory_distribution_test_get_value_as_json_string(
    glean_handle: u64,
    metric_id: u64,
    storage_name: FfiStr,
) -> *mut c_char {
    GLEAN.call_infallible(glean_handle, |glean| {
        MEMORY_DISTRIBUTION_METRICS.call_infallible(metric_id, |metric| {
            metric
                .test_get_value_as_json_string(glean, storage_name.as_str())
                .unwrap()
        })
    })
}
