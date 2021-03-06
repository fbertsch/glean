/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

@file:Suppress("FunctionNaming", "FunctionParameterNaming", "LongParameterList")

package mozilla.telemetry.glean.rust

import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.StringArray
import java.lang.reflect.Proxy
import mozilla.telemetry.glean.config.FfiConfiguration

// Turn a boolean into its Byte (u8) representation
internal fun Boolean.toByte(): Byte = if (this) 1 else 0

// Turn a Byte into a boolean where zero is false and non-zero is true
internal fun Byte.toBoolean(): Boolean = this != 0.toByte()

/**
 * Helper to read a null terminated String out of the Pointer and free it.
 *
 * Important: Do not use this pointer after this! For anything!
 */
internal fun Pointer.getAndConsumeRustString(): String {
    try {
        return this.getRustString()
    } finally {
        LibGleanFFI.INSTANCE.glean_str_free(this)
    }
}

/**
 * Helper to read a null terminated string out of the pointer.
 *
 * Important: doesn't free the pointer, use [getAndConsumeRustString] for that!
 */
internal fun Pointer.getRustString(): String {
    return this.getString(0, "utf8")
}

@Suppress("TooManyFunctions")
internal interface LibGleanFFI : Library {
    companion object {
        private val JNA_LIBRARY_NAME = "glean_ffi"

        internal var INSTANCE: LibGleanFFI = try {
            val lib = Native.load(JNA_LIBRARY_NAME, LibGleanFFI::class.java) as LibGleanFFI
            lib.glean_enable_logging()
            lib
        } catch (e: UnsatisfiedLinkError) {
            Proxy.newProxyInstance(
                LibGleanFFI::class.java.classLoader,
                arrayOf(LibGleanFFI::class.java)
            ) { _, _, _ ->
                throw IllegalStateException("Glean functionality not available", e)
            } as LibGleanFFI
        }
    }

    // Important: strings returned from rust as *mut char must be Pointers on this end, returning a
    // String will work but either force us to leak them, or cause us to corrupt the heap (when we
    // free them).

    // Glean top-level API

    fun glean_initialize(cfg: FfiConfiguration): Long

    fun glean_clear_application_lifetime_metrics(handle: Long)

    fun glean_test_clear_all_stores(handle: Long)

    fun glean_is_first_run(handle: Long): Byte

    fun glean_destroy_glean(handle: Long)

    fun glean_on_ready_to_submit_pings(handle: Long): Byte

    fun glean_enable_logging()

    fun glean_set_upload_enabled(glean_handle: Long, flag: Byte)

    fun glean_is_upload_enabled(glean_handle: Long): Byte

    fun glean_ping_collect(glean_handle: Long, ping_type_handle: Long): Pointer?

    fun glean_submit_pings_by_name(
        glean_handle: Long,
        ping_names: StringArray,
        ping_names_len: Int
    ): Byte

    fun glean_set_experiment_active(
        glean_handle: Long,
        experiment_id: String,
        branch: String,
        extra_keys: StringArray?,
        extra_values: StringArray?,
        extra_len: Int
    )

    fun glean_set_experiment_inactive(glean_handle: Long, experiment_id: String)

    fun glean_experiment_test_is_active(glean_handle: Long, experiment_id: String): Byte

    fun glean_experiment_test_get_data(glean_handle: Long, experiment_id: String): Pointer?

    // Ping type

    fun glean_new_ping_type(name: String, include_client_id: Byte, send_if_empty: Byte): Long

    fun glean_destroy_ping_type(handle: Long)

    fun glean_register_ping_type(glean_handle: Long, ping_type_id: Long)

    fun glean_test_has_ping_type(glean_handle: Long, name: String): Byte

    // Boolean

    fun glean_new_boolean_metric(
        category: String,
        name: String,
        send_in_pings: StringArray,
        send_in_pings_len: Int,
        lifetime: Int,
        disabled: Byte
    ): Long

    fun glean_destroy_boolean_metric(handle: Long)

    fun glean_boolean_set(glean_handle: Long, metric_id: Long, value: Byte)

    fun glean_boolean_test_get_value(glean_handle: Long, metric_id: Long, storage_name: String): Byte

    fun glean_boolean_test_has_value(glean_handle: Long, metric_id: Long, storage_name: String): Byte

    // Counter

    fun glean_new_counter_metric(
        category: String,
        name: String,
        send_in_pings: StringArray,
        send_in_pings_len: Int,
        lifetime: Int,
        disabled: Byte
    ): Long

    fun glean_destroy_counter_metric(handle: Long)

    fun glean_counter_add(glean_handle: Long, metric_id: Long, amount: Int)

    fun glean_counter_test_get_value(glean_handle: Long, metric_id: Long, storage_name: String): Int

    fun glean_counter_test_has_value(glean_handle: Long, metric_id: Long, storage_name: String): Byte

    fun glean_counter_test_get_num_recorded_errors(
        glean_handle: Long,
        metric_id: Long,
        error_type: Int,
        storage_name: String
    ): Int

    // Quantity

    fun glean_new_quantity_metric(
        category: String,
        name: String,
        send_in_pings: StringArray,
        send_in_pings_len: Int,
        lifetime: Int,
        disabled: Byte
    ): Long

    fun glean_destroy_quantity_metric(handle: Long)

    fun glean_quantity_set(glean_handle: Long, metric_id: Long, value: Long)

    fun glean_quantity_test_get_value(glean_handle: Long, metric_id: Long, storage_name: String): Long

    fun glean_quantity_test_has_value(glean_handle: Long, metric_id: Long, storage_name: String): Byte

    fun glean_quantity_test_get_num_recorded_errors(
        glean_handle: Long,
        metric_id: Long,
        error_type: Int,
        storage_name: String
    ): Int

    // String

    fun glean_new_string_metric(
        category: String,
        name: String,
        send_in_pings: StringArray,
        send_in_pings_len: Int,
        lifetime: Int,
        disabled: Byte
    ): Long

    fun glean_destroy_string_metric(handle: Long)

    fun glean_string_set(glean_handle: Long, metric_id: Long, value: String)

    fun glean_string_test_get_value(glean_handle: Long, metric_id: Long, storage_name: String): Pointer?

    fun glean_string_test_has_value(glean_handle: Long, metric_id: Long, storage_name: String): Byte

    fun glean_string_test_get_num_recorded_errors(
        glean_handle: Long,
        metric_id: Long,
        error_type: Int,
        storage_name: String
    ): Int

    // Datetime

    fun glean_new_datetime_metric(
        category: String,
        name: String,
        send_in_pings: StringArray,
        send_in_pings_len: Int,
        lifetime: Int,
        disabled: Byte,
        time_unit: Int
    ): Long

    fun glean_destroy_datetime_metric(handle: Long)

    fun glean_datetime_set(
        glean_handle: Long,
        metric_id: Long,
        year: Int,
        month: Int,
        day: Int,
        hour: Int,
        minute: Int,
        second: Int,
        nano: Long,
        offset_seconds: Int
    )

    fun glean_datetime_test_has_value(glean_handle: Long, metric_id: Long, storage_name: String): Byte

    fun glean_datetime_test_get_value_as_string(glean_handle: Long, metric_id: Long, storage_name: String): Pointer?

    fun glean_datetime_test_get_num_recorded_errors(
        glean_handle: Long,
        metric_id: Long,
        error_type: Int,
        storage_name: String
    ): Int

    // String list

    fun glean_new_string_list_metric(
        category: String,
        name: String,
        send_in_pings: StringArray,
        send_in_pings_len: Int,
        lifetime: Int,
        disabled: Byte
    ): Long

    fun glean_destroy_string_list_metric(handle: Long)

    fun glean_string_list_add(glean_handle: Long, metric_id: Long, value: String)

    fun glean_string_list_set(glean_handle: Long, metric_id: Long, values: StringArray, values_len: Int)

    fun glean_string_list_test_has_value(glean_handle: Long, metric_id: Long, storage_name: String): Byte

    fun glean_string_list_test_get_value_as_json_string(
        glean_handle: Long,
        metric_id: Long,
        storage_name: String
    ): Pointer?

    fun glean_string_list_test_get_num_recorded_errors(
        glean_handle: Long,
        metric_id: Long,
        error_type: Int,
        storage_name: String
    ): Int

    // UUID

    fun glean_new_uuid_metric(
        category: String,
        name: String,
        send_in_pings: StringArray,
        send_in_pings_len: Int,
        lifetime: Int,
        disabled: Byte
    ): Long

    fun glean_destroy_uuid_metric(handle: Long)

    fun glean_uuid_set(glean_handle: Long, metric_id: Long, value: String)

    fun glean_uuid_test_has_value(glean_handle: Long, metric_id: Long, storage_name: String): Byte

    fun glean_uuid_test_get_value(glean_handle: Long, metric_id: Long, storage_name: String): Pointer?

    // Timespan

    fun glean_new_timespan_metric(
        category: String,
        name: String,
        send_in_pings: StringArray,
        send_in_pings_len: Int,
        lifetime: Int,
        disabled: Byte,
        time_unit: Int
    ): Long

    fun glean_destroy_timespan_metric(handle: Long)

    fun glean_timespan_set_start(glean_handle: Long, metric_id: Long, start_time: Long)

    fun glean_timespan_set_stop(glean_handle: Long, metric_id: Long, stop_time: Long)

    fun glean_timespan_cancel(metric_id: Long)

    fun glean_timespan_set_raw_nanos(glean_handle: Long, metric_id: Long, elapsed_nanos: Long)

    fun glean_timespan_test_has_value(glean_handle: Long, metric_id: Long, storage_name: String): Byte

    fun glean_timespan_test_get_value(glean_handle: Long, metric_id: Long, storage_name: String): Long

    fun glean_timespan_test_get_num_recorded_errors(
        glean_handle: Long,
        metric_id: Long,
        error_type: Int,
        storage_name: String
    ): Int

    // TimingDistribution

    fun glean_new_timing_distribution_metric(
        category: String,
        name: String,
        send_in_pings: StringArray,
        send_in_pings_len: Int,
        lifetime: Int,
        disabled: Byte,
        time_unit: Int
    ): Long

    fun glean_destroy_timing_distribution_metric(handle: Long)

    fun glean_timing_distribution_set_start(metric_id: Long, start_time: Long): Long

    fun glean_timing_distribution_set_stop_and_accumulate(
        glean_handle: Long,
        metric_id: Long,
        timer_id: Long,
        stop_time: Long
    )

    fun glean_timing_distribution_cancel(metric_id: Long, timer_id: Long)

    fun glean_timing_distribution_accumulate_samples(glean_handle: Long, metric_id: Long, samples: LongArray?, len: Int)

    fun glean_timing_distribution_test_has_value(glean_handle: Long, metric_id: Long, storage_name: String): Byte

    fun glean_timing_distribution_test_get_value_as_json_string(
        glean_handle: Long,
        metric_id: Long,
        storage_name: String
    ): Pointer?

    fun glean_timing_distribution_test_get_num_recorded_errors(
        glean_handle: Long,
        metric_id: Long,
        error_type: Int,
        storage_name: String
    ): Int

    // MemoryDistribution

    fun glean_new_memory_distribution_metric(
        category: String,
        name: String,
        send_in_pings: StringArray,
        send_in_pings_len: Int,
        lifetime: Int,
        disabled: Byte,
        memory_unit: Int
    ): Long

    fun glean_destroy_memory_distribution_metric(handle: Long)

    fun glean_memory_distribution_accumulate(glean_handle: Long, metric_id: Long, sample: Long)

    fun glean_memory_distribution_accumulate_samples(glean_handle: Long, metric_id: Long, samples: LongArray?, len: Int)

    fun glean_memory_distribution_test_has_value(glean_handle: Long, metric_id: Long, storage_name: String): Byte

    fun glean_memory_distribution_test_get_value_as_json_string(
        glean_handle: Long,
        metric_id: Long,
        storage_name: String
    ): Pointer?

    fun glean_memory_distribution_test_get_num_recorded_errors(
        glean_handle: Long,
        metric_id: Long,
        error_type: Int,
        storage_name: String
    ): Int

    // CustomDistribution

    fun glean_new_custom_distribution_metric(
        category: String,
        name: String,
        send_in_pings: StringArray,
        send_in_pings_len: Int,
        lifetime: Int,
        disabled: Byte,
        range_min: Long,
        range_max: Long,
        bucket_count: Long,
        histogram_type: Int
    ): Long

    fun glean_destroy_custom_distribution_metric(handle: Long)

    fun glean_custom_distribution_accumulate_samples(glean_handle: Long, metric_id: Long, samples: LongArray?, len: Int)

    fun glean_custom_distribution_test_has_value(glean_handle: Long, metric_id: Long, storage_name: String): Byte

    fun glean_custom_distribution_test_get_value_as_json_string(
        glean_handle: Long,
        metric_id: Long,
        storage_name: String
    ): Pointer?

    fun glean_custom_distribution_test_get_num_recorded_errors(
        glean_handle: Long,
        metric_id: Long,
        error_type: Int,
        storage_name: String
    ): Int

    // Event

    fun glean_new_event_metric(
        category: String,
        name: String,
        send_in_pings: StringArray,
        send_in_pings_len: Int,
        lifetime: Int,
        disabled: Byte,
        allowed_extra_keys: StringArray?,
        allowed_extra_keys_len: Int
    ): Long

    fun glean_event_record(
        glean_handle: Long,
        handle: Long,
        timestamp: Long,
        extra_keys: IntArray?,
        extra_values: StringArray?,
        extra_len: Int
    )

    fun glean_event_test_has_value(glean_handle: Long, metric_id: Long, storage_name: String): Byte

    fun glean_event_test_get_value_as_json_string(
        glean_handle: Long,
        handle: Long,
        storage_Name: String
    ): Pointer?

    fun glean_event_test_get_num_recorded_errors(
        glean_handle: Long,
        metric_id: Long,
        error_type: Int,
        storage_name: String
    ): Int

    // Labeled Counter

    fun glean_new_labeled_counter_metric(
        category: String,
        name: String,
        send_in_pings: StringArray,
        send_in_pings_len: Int,
        lifetime: Int,
        disabled: Byte,
        labels: StringArray?,
        label_count: Int
    ): Long

    fun glean_labeled_counter_metric_get(handle: Long, label: String): Long

    fun glean_labeled_counter_test_get_num_recorded_errors(
        glean_handle: Long,
        metric_id: Long,
        error_type: Int,
        storage_name: String
    ): Int

    // Labeled Boolean

    fun glean_new_labeled_boolean_metric(
        category: String,
        name: String,
        send_in_pings: StringArray,
        send_in_pings_len: Int,
        lifetime: Int,
        disabled: Byte,
        labels: StringArray?,
        label_count: Int
    ): Long

    fun glean_labeled_boolean_metric_get(handle: Long, label: String): Long

    fun glean_labeled_boolean_test_get_num_recorded_errors(
        glean_handle: Long,
        metric_id: Long,
        error_type: Int,
        storage_name: String
    ): Int

    // Labeled string

    fun glean_new_labeled_string_metric(
        category: String,
        name: String,
        send_in_pings: StringArray,
        send_in_pings_len: Int,
        lifetime: Int,
        disabled: Byte,
        labels: StringArray?,
        label_count: Int
    ): Long

    fun glean_labeled_string_metric_get(handle: Long, label: String): Long

    fun glean_labeled_string_test_get_num_recorded_errors(
        glean_handle: Long,
        metric_id: Long,
        error_type: Int,
        storage_name: String
    ): Int

    // Misc

    fun glean_str_free(ptr: Pointer)
}

internal typealias MetricHandle = Long
