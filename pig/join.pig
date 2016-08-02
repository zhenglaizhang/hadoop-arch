/*
* Description of my program spanning
* multiple lines.
*
* These include the operators (LOAD, ILLUSTRATE), commands (cat,
  ls), expressions (matches, FLATTEN), and functions (DIFF, MAX)—
*/
A = LOAD 'input/pig/join/A';
B = LOAD 'input/pig/join/B';
C = JOIN A BY $0, /* ignored */ B BY $1;
DUMP C;