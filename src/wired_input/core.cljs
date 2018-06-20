(ns wired-input.core
  (:require [show.core    :as show]
            [show.dom     :as dom]
            [wire.up.show :as wired]
            [wire.core    :as w]))

(defn tap [wire component]
  (w/tap wire
     :form-change #(show/assoc! component :input (:value %))))

(show/defclass App [component]
  (initial-state [{:keys [base-input]}]
    {:wire (tap (w/wire) component)
     :input base-input })
  (render [props {:keys [wire input]}]
    (wired/input wire {:value input})))

(show/render-component
  (App {:base-input "A default string"})
  (.getElementById js/document "app"))
