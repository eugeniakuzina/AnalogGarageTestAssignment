# AnalogGarageTestAssignment

### What is missing for necessary system testing:
1. SMSes content
2. Phone number where SMSes were sent to
3. Monitor output written in file, not in console
4. Detailed info on each sender. I found this partially is available in DEBUG mode, but log formatting there is not ideal. To test each sender separately, the monitor would need to output details for each sender as well as consolidated info for all senders

### Found issues:
1. Monitor is showing 'rate' i.e. messages per second. The requirements are to show time per message
2. No validation on input params. If entered rate in config > 1, system fails with exception. Incorrect params should be handled gracefully

### Open questions:
1. 'Expected rate' value is not clearly defined. It's not defined in the requirements and it's calculated value doesn't make much sense. What is the value of it after all? 

### Implementation:
1. Test plan is implemented in Java because that's the language I'm most familiar with, but a better choice for this particular project would've been Python for available stat and data processing libraries
2. I manually copied the yml config and monitor output as files to the test project. In the real world I would prefer the test project to create yaml config per each scenario and run the system under test with this config. Each run would generate logs which would be validated
3. For the sake of simplicity I only automated tests with one Sender in config

### Statistical methods in testing:
 
1. Distribution of failed rate messages is binomial distribution. It can be verified with one sample Z-test because we can generate samples with N > 30. Null hypothesis is failed_rate = failed_rate from Senders config. Alternative hypothesis is failed rate calculated from Monitor output <> failed_rate from config. Since this is an emulator, I consider this a two-tailed test, as it is important for the emulator to emulate exactly within configured range. If the system under test were a real life system, I'd consider it a one-tailed test (i.e. fail rate << threshold is not an issue in real life, but an issue for an emulator). We can calculate standard deviation for binomial distribution using existing data
2. Distribution of message delays is gamma distribution. It can be verified with one sample Z-test since we can generate samples with N > 30. Null hypothesis is mean_time = mean_time from Senders config. Alternative hypothesis is time per message calculated from Monitor output <> mean_time from config. In current implementation Monitor doesn't provide enough data points to calculate standard deviation for gamma distribution, because data is grouped by seconds and it't not easy to get data grouped by message delays (NOTE: It is possible to parse data points from Monitor with report_period: 0.01, however it'll be quite cumbersome and it's best to update Monitor output) 
3. For the sake of the test I decided to set confidence level to 99%. In real work environment this value might've been different and I'd discuss it with business owner 

### Test plan:
1. Implemented: Verify number of sent messages. Compare expected (num_messages in config) with actual (sent messages in monitor)
2. Verify report period. Compare expected (report_period in config) with actual time stamps in the monitor output 
3. Implemented for one sender: Verify expected rate. Compare expected (mean_time) and actual expected rate (1 / Expected rate) from the monitor. This test scenario might change based on the answer to the open question
4. Verify expected rate for multiple senders. Compare expected (sum of mean_time for each sender) and actual expected rate from the monitor (1 / Expected rate). This test scenario might change based on the answer to the open question. 
5. Implementer for one sender: Verify fail rate for all senders. Using Z-test for binomial distribution with 99% confidence level compare expected value (failure_rate from sender config) and calculated failure rate from monitor statistics.
6. Verify fail rate for each individual sender. If monitor output would be improved to show detailed statistics of sent messages for each sender, it would be possible to compare failure_rate from config for each sender to calculated failure rate from monitor statistics using Z-test for binomial distribution with 99% confidence level
7. Verify time per message for all senders. If monitor output is improved to show data points for each message, then using statistical Z-test for gamma distribution with 99% confidence level compare expected value (mean_time from sender config) and calculated time per message from monitor statistics
8. Verify time per message for each individual sender. If monitor output would be improved to show detailed statistics of sent messages for each sender, it would be possible to compare mean_time from config for each sender to calculated time per message from monitor statistics using statistical Z-test for gamma distribution with 99% confidence level
9. Verify SMS content. Verify SMS contains up to 100 random symbols
10. Verify generated random phone number is a valid phone number
11. Verify incorrect input parameters are handled gracefully
