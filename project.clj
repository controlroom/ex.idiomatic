(defproject controlroom/ex.idiomatic "0.1.0"
  :description "How to use ctrlrm projects"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2202"]]

  :plugins [[lein-cljsbuild "1.0.3"]]

  :cljsbuild
  {:builds
   [{:id "basic_wire"
     :source-paths ["examples/basic_wire/src"]
     :compiler {:output-dir "examples/basic_wire/out"
                :output-to  "examples/basic_wire/main.js"
                :optimizations  :none
                :output-wrapper false
                :source-map     true }}
    {:id "basic_wired"
     :source-paths ["examples/basic_wired/src"]
     :compiler {:output-dir "examples/basic_wired/out"
                :output-to  "examples/basic_wired/main.js"
                :optimizations  :none
                :output-wrapper false
                :source-map     true }}
    ]})

