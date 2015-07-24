(ns examples.basic-plug.core
  (:require [show.core  :as show :include-macros true]
            [show.dom   :as dom  :include-macros true]
            [plug2.core :as plug]
            [plug2.events :as events]))

(enable-console-print!)

(def company-schema
  (plug/schema {:entites {:contacts {:cardinality :many}}}))

(def companies
  (conj (plug/collection company-schema)
        {:id "c1"
         :name "facebook"
         :contacts {1 {:id 1 :name "Mike Schmoker"}
                    2 {:id 2 :name "Lelien Dingle"}}}))

(show/defclass Contact [component]
  (render [{:keys [contact]} state]
          (dom/li
            (dom/p {:onClick
                    (fn [] (update-in contact [:name] (plug/valfn str " If you want!")))}
                   (:name contact)))))

;; props
;; callables
;;
(show/defclass PlugMixin [component]
  (did-mount []
    (js/console.log component)
    (println "did-mount : "
             (vals (show/get-props component))))
  (will-receive-props [next-props]
    (println "will-receive-props: " next-props))
  (will-update [next-props next-state]
    (println "will-update : " next-props next-state))
  (should-update [next-props next-state]
    (println (vals (.-props component)))
    (println (vals next-props))
    (println "should-update : " next-props next-state))
  (did-update [prev-props prev-state]
    (println "did-update: " prev-props prev-state)))

;; (initial-state
;;   {:companies (-> component
;;                   (show/get-props :companies)
;;                   (events/listen :plug2.data/update
;;                                  #(show/force-update! component)))})
(show/defclass Main [component]
  (mixins PlugMixin)
  ;; (super-duper [a b])
  ;;
  ;; (plug-listen [{:keys [companies]}]
  ;;   [companies :plug2.data/update #(show/force-update! component)])
  ;;
  (initial-state
    {:companies (-> component
                    (show/get-props :companies)
                    (events/listen :plug2.data/update
                                   #(show/force-update! component)))})
  (render [state {:keys [companies]}]
          (dom/ul
            (for [company companies]
              (dom/li
                (dom/p
                  {:onClick (fn [] (update-in company [:name]
                                              (plug/valfn str " - go")))}
                  (:name company))
                (dom/ul
                  (for [contact (:contacts company)]
                    (Contact {:contact contact :key (gensym)}))))))))

(show/render-component
  (Main {:companies companies :key (gensym)})
  (.getElementById js/document "app"))
