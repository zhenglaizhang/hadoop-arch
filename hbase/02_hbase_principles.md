## Table Format

* HBase introduces the concept of **column families**
* System tables
* User tables (default namespace by default)

## Table Layout

* An HBase table is composed of **one or more column families (CFs)**, containing columns (which we will call **columns qualifiers,** or CQ for short) where **values can be stored**.
* HBase tables may be sparsely populated—some columns might not contain a value at all.
    * There is **no null value stored** in anticipation of a future value being stored. Instead, **that column for that particular row key simply does not get added to the table**. 


> a row is formed of multiple columns, all referenced by
  the same key. **A specific column and a key are called a cell**. It is possible
  to have **multiple versions of the same cell**, all differentiated by
  the **timestamp**. A cell can also be called a **KeyValue pair**. So a row,
  referenced by a key, is formed of a group of cells, each being a specific
  column for the given row.

* Only columns where there is a value are stored in the underlying filesystem
* Columns can be dynamically generated as data is inserted into the column family.
* it is possible to have millions of columns with dynamically created names, where columns names are different between all the rows.
* To allow a faster lookup access, **keys and columns are alphabetically sorted within a table but also in memory**.
* HBase orders the keys based on the byte values, “1234” will come before “9”.
* If you have to store numbers, then to **save on space and to keep the ordering, you will need to store their byte representation**.
* they will be used to create files and directories in the filesystem, the table name and the column family names need to use only printable characters.

![Logical representation of an HBase table](.02_hbase_principles_images/logic_representation.png)

## Table Storage
* a table is composed of one to many regions, composed of one to many column families, composed of a single store, composed of a unique memstore plus one to many HFiles, composed of blocks composed of cells.
* On a RegionServer, you will have as many **memstores as you have regions multiplied by the number of column families receiving writes,** all sharing the same reserved memory area.