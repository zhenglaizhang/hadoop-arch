-- Finds the maximum temperature by year

-- Pig Latin is a data flow programming language,
-- whereas SQL is a declarative programming language

records = LOAD 'input/ncdc/micro-tab/sample.txt'
    AS (year: chararray, temperature:int, quality:int);

filtered_records = FILTER records BY temperature != 999 AND
    quality IN (0, 1, 4, 5, 9);

grouped_records = GROUP filtered_records BY year;
max_temp = FOREACH grouped_records GENERATE group,  MAX(filtered_records.temperature);

DUMP max_temp;