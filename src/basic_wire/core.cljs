(ns basic-wire.core
  (:require [show.core    :as show]
            [show.dom     :as dom]
            [wire.core    :as w]))

(show/defclass Widget [component]
  (render [{:keys [selected wire name]} _]
    (dom/div
      (dom/button {:onClick #(w/act wire :clickedit)} name)
      (dom/p {:style {:display (if selected "block" "none")}}
             "You Selected me!"))))

(defn tap-widget-wire [wire component]
  (w/tap wire
    :clickedit #(show/assoc! component :selected-widget (:id %))))

(show/defclass App [component]
  (initial-state []
    {:selected-widget nil
     :widgets (for [i (range 20)] (str "weejit-" i))
     :wire (w/wire)})
  (render [params {:keys [widgets wire selected-widget]}]
    (dom/div
      (let [widget-wire (tap-widget-wire wire component)]
        (map-indexed (fn [idx name]
                       (Widget {:wire (w/lay widget-wire nil {:id idx})
                                :selected (= selected-widget idx)
                                :name name}))
                     widgets)))))

(show/render-component
  (App)
  (.getElementById js/document "app"))
