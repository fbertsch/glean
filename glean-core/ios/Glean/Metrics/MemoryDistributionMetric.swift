/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

import Foundation

/// This implements the developer facing API for recording memory distribution metrics.
///
/// Instances of this class type are automatically generated by the parsers at build time,
/// allowing developers to record values that were previously registered in the metrics.yaml file.
///
/// The memory distribution API only exposes the `MemoryDistributionMetricType.accumulate()` method.
public class MemoryDistributionMetricType {
    let handle: UInt64
    let disabled: Bool
    let sendInPings: [String]

    /// The public constructor used by automatically generated metrics.
    public init(category: String,
                name: String,
                sendInPings: [String],
                lifetime: Lifetime,
                disabled: Bool,
                memoryUnit: MemoryUnit) {
        self.disabled = disabled
        self.sendInPings = sendInPings
        self.handle = withArrayOfCStrings(sendInPings) { pingArray in
            glean_new_memory_distribution_metric(
                category,
                name,
                pingArray,
                Int32(sendInPings.count),
                lifetime.rawValue,
                disabled.toByte(),
                memoryUnit.rawValue
            )
        }
    }

    /// Destroy this metric.
    deinit {
        if self.handle != 0 {
            glean_destroy_memory_distribution_metric(self.handle)
        }
    }

    /// Record a single value, in the unit specified by `memoryUnit`, to the distribution.
    ///
    /// - parameters:
    ///    * sample: the value
    public func accumulate(_ sample: UInt64) {
        guard !self.disabled else { return }

        Dispatchers.shared.launchAPI {
            return glean_memory_distribution_accumulate(self.handle, sample)
        }
    }

    /// Tests whether a value is stored for the metric for testing purposes only. This function will
    /// attempt to await the last task (if any) writing to the the metric's storage engine before
    /// returning a value.
    ///
    /// - parameters:
    ///     * pingName: represents the name of the ping to retrieve the metric for.
    ///                 Defaults to the first value in `sendInPings`.
    /// - returns: true if metric value exists, otherwise false.
    public func testHasValue(_ pingName: String? = nil) -> Bool {
        Dispatchers.shared.assertInTestingMode()

        let pingName = pingName ?? self.sendInPings[0]
        return glean_memory_distribution_test_has_value(self.handle, pingName).toBool()
    }

    /// Returns the stored value for testing purposes only. This function will attempt to await the
    /// last task (if any) writing to the the metric's storage engine before returning a value.
    ///
    /// Throws a "Missing value" exception if no value is stored
    ///
    /// -parameters:
    ///   * pingName: represents the name of the ping to retrieve the metric for.
    ///               Defaults to the first value in `sendInPings`.
    ///
    /// - returns: value of the stored metric
    public func testGetValue(_ pingName: String? = nil) throws -> DistributionData {
        Dispatchers.shared.assertInTestingMode()

        let pingName = pingName ?? self.sendInPings[0]

        if !testHasValue(pingName) {
            throw "Missing value"
        }

        let json = String(
            freeingRustString: glean_memory_distribution_test_get_value_as_json_string(
                self.handle,
                pingName
            )
        )

        return DistributionData(fromJson: json)!
    }

    /// Returns the number of errors recorded for the given metric.
    ///
    /// - parameters:
    ///     * errorType: The type of error recorded.
    ///     * pingName: represents the name of the ping to retrieve the metric for.
    ///                 Defaults to the first value in `sendInPings`.
    ///
    /// - returns: The number of errors recorded for the metric for the given error type.
    public func testGetNumRecordedErrors(_ errorType: ErrorType, pingName: String? = nil) -> Int32 {
        Dispatchers.shared.assertInTestingMode()

        let pingName = pingName ?? self.sendInPings[0]

        return glean_memory_distribution_test_get_num_recorded_errors(
            self.handle,
            errorType.rawValue,
            pingName
        )
    }
}
