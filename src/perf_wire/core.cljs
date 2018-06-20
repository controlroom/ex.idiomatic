(ns perf-wire.core
  (:require [show.core    :as show]
            [show.dom     :as dom]
            [wire.up.show :as wired]
            [wire.core    :as w]))

(defn wiretap [component]
  (w/taps (w/wire)
    :mouse-click
    (fn [evt]
      (println "clicked")
      (show/assoc! component :selected (:id evt)))))

(show/defclass Boxes [component]
  (render [{:keys [wire]} state]
    (dom/div
      (for [i (range 5000)]
        (wired/div (w/lay wire nil {:id i})
                   {:className "box"
                    :key (str "box-" i)} i)))))

(show/defclass Heading [component]
  (render [{:keys [selected]} _]
    (dom/h2 selected)))

(show/defclass App [component]
  (initial-state
    {:selected nil
     :hovered nil
     :wire (wiretap component)})
  (render [props {:keys [wire selected hovered]}]
    (dom/div
      (Heading {:selected selected
                :key "heading"})
      (Boxes {:wire wire
              :key "boxes"}))))

(show/render-component
  (App)
  (.getElementById js/document "app"))
