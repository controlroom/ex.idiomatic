(ns examples.mouse.core
  (:require [show.core      :as show  :include-macros true]
            [show.dom       :as dom   :include-macros true]
            [wire.up.dom    :refer [wire-up]]
            [wire.core      :as w]))

(defn update-coords [component event]
  (show/assoc! component :coords [(.-clientX event) (.-clientY event)]))

(show/defclass MouseView [component]
  (will-mount []
    (-> (w/wire)
        (w/tap :mouse-move #(update-coords component (:event %)))
        (wire-up js/window)))
  (render [props {:keys [coords]}]
    (dom/p (pr-str coords))))

(show/render-component
  (MouseView)
  (.getElementById js/document "app"))
