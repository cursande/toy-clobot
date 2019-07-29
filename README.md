# toy-clobot

Simulates a small lonely robot on a table.

## Usage

Invoke it with `lein run` and pass it the path to a file containing the
following commands:

```
PLACE X,Y,F
MOVE
LEFT
RIGHT
REPORT
```

An example command list can be found in `test/fixtures/instructions.txt`.
