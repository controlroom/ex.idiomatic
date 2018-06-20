# idiomatic ctrlrm usage

Just a set of examples showing how to use ctrlrm libraries. These vary in type
and scope. Enjoy!

## Examples

- [Basic Wire](src/basic_wire) [show, wire] - Just the basics of wire and show
- [Basic Wired](src/basic_wired) [show, wire] - Basic Wire, using wired dom components
- [Wired Input](src/wired_input) [show, wire] - Hooking up an input element
- [Perf Wire](src/perf_wire) [show, wire] - Wire handling thousands of events
- [Mouse](src/mouse) [show, wire] - Using wired window events to drive display of mouse data
- [Default Props](src/default_props) [show, wire] - Example with using default props
- [Simple Todo](src/simple_todo) [show, wire] - Basic todo with animation
- [TodoMVC](src/todomvc) [show, wire] - Obligatory TodoMVC example

## Building

You can build any of these examples using this simple command line script

```$> ./scripts/build $exmaple_name```

You will have to then start a webserver at the target project. I recommend:

```$> cd target; python2 -m SimpleHTTPServer 3030```

Then you can view the examples at http://localhost:3030/$example_name

## License

Copyright © 2018 controlroom.io

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
