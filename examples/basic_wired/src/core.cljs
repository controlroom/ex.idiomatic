(ns examples.basic-wired.core
  (:require [show.core    :as show :include-macros true]
            [show.dom     :as dom  :include-macros true]
            [wire.up.show :as wired :include-macros true]
            [wire.core    :as w]))

(show/defclass Widget [component]
  (render [{:keys [selected wire name]} _]
    (dom/div
      (wired/button wire name)
      (dom/p {:className (show/class-map {"hidden" (not selected)})}
             "You Selected me!"))))

(defn tap-widget-wire [wire component]
  (w/tap wire :mouse-click #(show/assoc! component :selected-widget (:id %))))

(show/defclass App [component]
  (initial-state []
    {:selected-widget nil
     :widgets (for [i (range 20)] {:name (str "weejit-" i)})
     :wire (w/wire)})
  (render [params {:keys [widgets wire selected-widget]}]
    (dom/div
      (let [widget-wire (tap-widget-wire wire component)]
        (map-indexed #(Widget {:wire (w/lay widget-wire nil {:id %1})
                               :selected (= selected-widget %1)
                               :name (:name %2)})
                     widgets)))))

(show/render-component
  (App)
  (.getElementById js/document "app"))
