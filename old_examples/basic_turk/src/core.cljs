(ns examples.basic-turk.core
  (:require [show.core :as show :include-macros true]
            [show.dom  :as dom  :include-macros true]
            [turk.core :as turk]
            [wire.up.show :as wired :include-macros true]
            [wire.core :as w]))

(show/defclass MainNav [component]
  (render [params state]
    (dom/div
      (:children params))))

(def root-nav (turk/nav nil (MainNav)))

(show/defclass PeopleNav [component]
  (render [params state]
    (dom/div
      (dom/li "People")
      (if (:focused? params)
        (dom/li "All People")
        (dom/li "Young People")
        (dom/li "Old People"))
      (:children params))))

(def people-nav (turk/nav root-nav PeopleNav))

(show/defclass PersonNav [component]
  (render [params state]
    (dom/div
      (dom/li (:person-name params))
      (if (:focused? params)
        (dom/li "Age")
        (dom/li "Favorite Taco Meat"))
      (:children params))))

(def person-nav (turk/nav people-nav PersonNav))

(show/defclass App [component]
  (render [params state]
    (dom/div
      (turk/nav-component root-nav)
      (turk/root-component root-nav))))

(show/render-component
  (App)
  (.getElementById js/document "app"))

(defn tap-people-nav [wire]
  (w/taps (w/wire)))

(show/defclass PeopleView [component]
  (render [params state]
    (dom/p "people-view")))

