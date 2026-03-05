# Common Data Engineering Interview Questions

For a Senior backend developer pivoting into or encountering Data Engineering questions, interviews focus less on "can you build a REST API" and more on "how do you handle data at terrifying scale."

Here are the classic questions categorized by domain.

## 1. Spark Execution & Optimization

**Q: Explain the difference between a Transformation and an Action in Spark. Give an example of why this distinction matters.**
*   *Answer Topic:* Lazy evaluation. Explain that Transformations (`map`, `filter`) build a logical DAG, but nothing happens until an Action (`count`, `save`) is called. Give the "filtering a 1TB file down to 10 rows" example.

**Q: Your Spark job is crashing with an `OutOfMemoryError` during a JOIN. What is the most likely cause and how do you fix it?**
*   *Answer Topic:* Data Skew causing a massive Shuffle to a single partition/executor.
*   *Solution:* 
    1.  **Broadcast Join:** If one of the tables is small (e.g., a "Country Codes" lookup table), use a Broadcast Hash Join. Spark sends a full copy of the small table to *every* node, completely eliminating the need to shuffle the massive multi-terabyte table over the network.
    2.  **Salting:** If both tables are huge, artificially append random numbers (salts) to the skewed key to force the data to spread evenly across the cluster.

**Q: Explain what a DAG is in the context of Spark.**
*   *Answer Topic:* Directed Acyclic Graph. It's the physical execution plan that the Catalyst Optimizer creates. "Acyclic" means data flows in one direction; it cannot loop back on itself.

## 2. Architecture & Paradigms

**Q: When would you recommend an ELT approach over traditional ETL?**
*   *Answer Topic:* Modern cloud data warehouses (Snowflake, BigQuery). When storage is cheap and compute inside the destination warehouse scales infinitely. ELT allows you to load raw data quickly and use standard SQL to transform it later, preserving the raw data in a Data Lake (like S3) just in case a transformation rule was wrong.

**Q: What is the difference between a Data Warehouse and a Data Lake?**
*   *Answer Topic:* Structure. Warehouse = Schema-on-Write, processed/curated data, structured tables, highly optimized for business intelligence queries. Lake = Schema-on-Read, raw unstructured/semi-structured files (JSON/Parquet/CSV/Images) dumped in blob storage (AWS S3) for exploratory data science.

**Q: How do you handle "Late-Arriving Data" in a streaming application?**
*   *Answer Topic:* Watermarking. Discuss how systems like Spark Structured Streaming or Flink maintain state for a certain "window" of time. If a mobile event generated at 1:00 PM doesn't hit the server until 1:05 PM due to a lost signal, a "5-minute watermark" allows the engine to keep the 1:00 PM aggregation window open to include it before finally finalizing the result.

## 3. SQL / Analytical Queries
Data Engineering interviews will test advanced SQL far more than typical backend interviews.

**Q: Write a query to find the employee with the second-highest salary in each department.**
*   *Answer Topic:* Window Functions (specifically `RANK()` or `DENSE_RANK()`).
*   *Example:* 
    ```sql
    WITH RankedSalaries AS (
        SELECT emp_id, dept_id, salary,
               DENSE_RANK() OVER(PARTITION BY dept_id ORDER BY salary DESC) as rank
        FROM Employees
    )
    SELECT * FROM RankedSalaries WHERE rank = 2;
    ```

**Q: Explain the difference between `RANK()`, `DENSE_RANK()`, and `ROW_NUMBER()`.**
*   *Answer Topic:* How they handle ties. If two people have the exact same highest salary:
    *   `ROW_NUMBER()`: Gives them 1 and 2 arbitrarily. Next person is 3.
    *   `RANK()`: Gives them both 1. The next person is 3. (Skips 2).
    *   `DENSE_RANK()`: Gives them both 1. The next person is 2. (No gaps).
