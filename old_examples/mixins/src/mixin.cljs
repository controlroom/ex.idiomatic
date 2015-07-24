(ns examples.mixins.core
  (:require [show.core :as show :include-macros true]
            [show.dom  :as dom  :include-macros true]))

(enable-console-print!)

(show/defclass MountedTimeState [component]
  (awesome-town
    (println "testing"))
  (will-mount
    (println (show/get-state component :registered-stores))))

(show/defclass Seconds [component]
  (mixins MountedTimeState)
  (initial-state []
    {:registered-stores ['store.rad
                         'store.awesome]})
  (render [props {:keys [current-seconds]}]
    (aset js/window "awesome" component)
    (js/console.log "huh?")
    (dom/p current-seconds)))


(show/render-component
  (Seconds)
  (.getElementById js/document "app"))
