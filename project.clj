(declare create-build)
(defproject controlroom/idiomatic "0.1.0"
  :description "How to use ctrlrm projects as they were intended"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2202"]]

  :plugins [[lein-cljsbuild "1.0.3"]]

  :cljsbuild {:builds
   (map create-build ["basic_wire"])})

(defn create-build [id]
  {:id id
   :source-paths [(str "examples/" id "/src")]
   :compiler {:output-dir (str "examples/" id "/out")
              :output-to  (str "examples/" id "/main.js")
              :optimizations  :none
              :output-wrapper false
              :source-map     true }})
