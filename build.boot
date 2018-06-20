(set-env!
 :resource-paths #{"example"}
 :dependencies '[[adzerk/boot-cljs   "0.0-3308-0"]
                 [adzerk/boot-reload "0.2.6"]
                 [pandeiro/boot-http "0.6.3"]

                 [controlroom/show "0.6.0-SNAPSHOT"]
                 [controlroom/wire "0.1.1"]
                 ;; [controlroom/plug "0.1.0-SNAPSHOT"]
                 [controlroom/plug-neo "0.1.0-SNAPSHOT"]
                 ])

(require
  '[adzerk.boot-cljs    :refer [cljs]]
  '[adzerk.boot-reload  :refer [reload]]
  '[pandeiro.boot-http  :refer [serve]])

(deftask run []
  (comp (serve) (cljs) (wait)))

(deftask dev []
  (comp (serve)
        (watch)
        (checkout :dependencies [['controlroom/show "0.6.0-SNAPSHOT"]
                                 ['controlroom/wire "0.1.1"]
                                 ;; ['controlroom/plug "0.1.0-SNAPSHOT"]
                                 ['controlroom/plug-neo "0.1.0-SNAPSHOT"]])
        (reload)
        (cljs)))
