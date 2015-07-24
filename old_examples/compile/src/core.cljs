(ns examples.compile.core
  (:require [show.core :as show :include-macros true]
            [show.dom  :as dom  :include-macros true]
            [plug.core :as plug]
            [plug.pub-sub :as pub-sub]
            [plug.meta :as meta]
            [plug.data :as data]))

;; (def the-plug
;;   (plug/plug :base))
;;
;; (show/defclass App [component]
;;   (will-mount
;;     (println (data/new! (show/get-props component :plug) {})))
;;   (render [p s]
;;           (dom/div "radical")))

(def the-plug
  (-> (plug/plug :base)
      (data/=>defaults {:name "I Have No Name!!"})))

(show/defclass App [component]
  (will-mount
    (show/assoc! component :new-one
                 (data/new! (show/get-props component :plug) {})))
  (render [params state]
          (dom/div (data/get (:new-one state) :name))))


(show/render-component
  (App {:plug the-plug})
  (.getElementById js/document "app"))
