package at.tuwien.genben;

import at.tuwien.genben.xml.TestCase;

/**
 * interface which a TestDriver needs to implement
 */
public interface TestDriver
{
    /**
     * should prepare a testcase (eg. connect to a database, open files)
     * @param testCase
     */
    void prepare(TestCase testCase);

    /**
     * for java benchmarks it is importend to do some warmup, as the
     * virtual machine does some runtime optimizations for code which gets
     * executed more often
     * @param testCase
     */
    void warmup(TestCase testCase);

    /**
     * gets called if the driver should run his test sequence
     * @param testCase
     */
    void run(TestCase testCase);

    /**
     * finish gets called if a testcase reached its end
     * the driver can do some cleanup if it is needed (e.g. delete files)
     * @param testCase
     */
    void finish(TestCase testCase);
}
