(ns default-props.core
  (:require [show.core      :as show]
            [show.dom       :as dom]
            [wire.up.show   :as wired]
            [wire.core      :as w]))

(enable-console-print!)

(show/defclass HeyThere!! [component]
  (default-props
    {:hey "what?"})
  (render [{:keys [a hey]} _]
    (js/console.log hey)
    (dom/div
      (dom/p a)
      (dom/p hey))))

(show/defclass BasicDefaultProps [component]
  (initial-state
    {:num 12})
  (will-mount
    (js/setTimeout
      (fn []
        (println "associng!")
        (show/assoc! component :num 14))
      1000))
  (render [{:as props :keys [what]} {:keys [num]}]
    (println "rendering HeyThere!!" num)
    (HeyThere!! {:key "heythere"
                 :a num})))

(show/render-component
  (BasicDefaultProps)
  (.getElementById js/document "app"))
