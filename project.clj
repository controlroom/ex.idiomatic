(defproject controlroom/ex.idiomatic "0.2.0"
  :description "How to use ctrlrm projects"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.238"]
                 [controlroom/show "0.7.0"]
                 [controlroom/wire "0.2.0"]
                 [cljsjs/react "16.4.0-0"]
                 [cljsjs/react-dom "16.4.0-0"]
                 [cljsjs/create-react-class "15.6.3-0"]
                 [cljsjs/react-transition-group "2.3.1-0"]]

  :plugins [[lein-cljsbuild "1.1.7"]]

  :cljsbuild
  {:builds
   [{:id "basic_wire"
     :source-paths ["src/basic_wire"]
     :compiler {:output-dir "target/basic_wire/out"
                :asset-path "/out"
                :output-to  "target/basic_wire/main.js"}}
    {:id "basic_wired"
     :source-paths ["src/basic_wired"]
     :compiler {:output-dir "target/basic_wired/out"
                :asset-path "/out"
                :output-to  "target/basic_wired/main.js"}}
    {:id "wired_input"
     :source-paths ["src/wired_input"]
     :compiler {:output-dir "target/wired_input/out"
                :asset-path "/out"
                :output-to  "target/wired_input/main.js"}}
    {:id "perf_wire"
     :source-paths ["src/perf_wire"]
     :compiler {:output-dir "target/perf_wire/out"
                :asset-path "/out"
                :output-to  "target/perf_wire/main.js"}}
    {:id "default_props"
     :source-paths ["src/default_props"]
     :compiler {:output-dir "target/default_props/out"
                :asset-path "/out"
                :output-to  "target/default_props/main.js"}}
    {:id "mixins"
     :source-paths ["src/mixins"]
     :compiler {:output-dir "target/mixins/out"
                :asset-path "/out"
                :output-to  "target/mixins/main.js"}}
    {:id "mouse"
     :source-paths ["src/mouse"]
     :compiler {:output-dir "target/mouse/out"
                :asset-path "/out"
                :output-to  "target/mouse/main.js"}}
    {:id "simple_todo"
     :source-paths ["src/simple_todo"]
     :compiler {:output-dir "target/simple_todo/out"
                :asset-path "/out"
                :output-to  "target/simple_todo/main.js"}}
    {:id "todomvc"
     :source-paths ["src/todomvc"]
     :compiler {:output-dir "target/todomvc/out"
                :asset-path "/out"
                :output-to  "target/todomvc/main.js"}}]})
