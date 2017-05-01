# Weekend One-Offs

Collection of experimental one-off projects.

## Cellular
Conway's Game of Life implemented on the GPU. [Source](https://github.com/TerryTsai/Weekend-One-Offs/tree/master/js/cellular) , 
[Demo](https://cdn.rawgit.com/TerryTsai/Weekend-One-Offs/6c8ca0ce2af93ba0ffd59c738f197ea0c3ec0770/js/cellular/cellular.html)

<img src="https://github.com/TerryTsai/Weekend-One-Offs/blob/master/js/cellular/screen.png" width="400">

## Flappy
Flappy Divs. [Source](https://github.com/TerryTsai/Weekend-One-Offs/tree/master/js/flappy) , 
[Demo](https://cdn.rawgit.com/TerryTsai/Weekend-One-Offs/0975d516/js/flappy/fb.html)

<img src="https://github.com/TerryTsai/Weekend-One-Offs/blob/master/js/flappy/1488951196.png" width="400">

## Caps
Audio visualizer for Youtube videos. [Source](https://github.com/TerryTsai/Weekend-One-Offs/tree/master/js/visualizer)

<img src="https://github.com/TerryTsai/Weekend-One-Offs/blob/master/js/visualizer/1488947726.png" width="400">


## Automata
Generic NFA builder for sequence matching. [Source](https://github.com/TerryTsai/Weekend-One-Offs/tree/master/java/automata)
```java
// Matches 'a','b','c' repeated any number of times.
Node<Character> a = new Node<>();
Node<Character> b = new Node<>();
Node<Character> c = new Node<>();
Node<Character> d = new Node<>();
a.add('a', b);
b.add('b', c);
c.add('c', d);
NFA<Character> nfa = NFA.repeat(new NFA<>(a, d));
nfa.accepts('a','b','c');		// true
nfa.accepts('a','b','c','d');	// false
```

```java
// Ensures any open task is paired with a close task.
// Can not open or close a resource twice.
Runnable open = () -> System.out.println("Opening Resource.");
Runnable close = () -> System.out.println("Closing Resource.");
Runnable other = () -> System.out.println("Some Other Runnable.");
Node<Runnable> a = new Node<>();
Node<Runnable> b = new Node<>();
Node<Runnable> c = new Node<>();
a.add(open, b);
a.add(close, c);
a.addDefault(a);
b.add(close, a);
b.add(open, c);
b.addDefault(b);
NFA<Runnable> nfa = new NFA<>(a, a);
nfa.accepts(open, close);           // true
nfa.accepts(open, other, close);    // true
nfa.accepts(open, close, close);    // false
nfa.accepts(close, open);           // false
```

## Codec
Simple binary & hexadecimal String codecs. [Source](https://github.com/TerryTsai/Weekend-One-Offs/tree/master/java/codec)

## Kinect
Pulling image & depth data from Kinect 1 and rendering to JavaFX application. [Source](https://github.com/TerryTsai/Weekend-One-Offs/tree/master/java/kinect)

<img src="https://github.com/TerryTsai/Weekend-One-Offs/blob/master/java/kinect/149035625428706.png" width="400">

## Sudoku
Brute-force sudoku solver for any sized board. [Source](https://github.com/TerryTsai/Weekend-One-Offs/tree/master/java/sudoku)
```java
	new SudokuSolver(new int[]{
        0, 5, 0, 2, 0, 0, 4, 0, 9,
        0, 3, 0, 0, 6, 0, 5, 0, 0,
        0, 0, 0, 0, 0, 3, 0, 0, 7,
        0, 0, 3, 0, 0, 0, 0, 0, 4,
        5, 0, 2, 4, 8, 6, 3, 0, 1,
        8, 0, 0, 0, 0, 0, 6, 0, 0,
        1, 0, 0, 8, 0, 0, 0, 0, 0,
        0, 0, 5, 0, 4, 0, 0, 2, 0,
        7, 0, 9, 0, 0, 5, 0, 4, 0
	}).showSolution(true);
```
