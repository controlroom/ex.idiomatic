(ns todomvc.app
  (:import goog.History)
  (:require [show.core :as show :include-macros true]
            [show.dom  :as dom  :include-macros true]
            [wire.up.show :as wired :include-macros true]
            [wire.core :as w]

            [goog.events :as events]
            [goog.history.EventType :as EventType]

            [todomvc.store :as store]))

(enable-console-print!)

;; Header
;;
(defn header-wire []
  (w/taps (w/wire)
    [:keyboard-up :keypress-enter]
    (fn [evt]
      (when (not= (:value evt) "")
        (store/create-todo (.trim (:value evt)))
        (aset (:target evt) "value" "")))))

(show/defclass Header [component]
  (render [props state]
    (dom/header {:id "header"}
      (dom/h1 "todos")
      (wired/input (header-wire) {:id "new-todo"
                                  :placeholder "What needs to be done?"
                                  :autoFocus true}))))

;; Item
;;
(defn item-wire-tap [component]
  (w/taps (w/wire)
    [:.toggle :mouse-click]
      (fn [evt]
        (store/toggle-complete (:id evt)))
    [:.destroy :mouse-click]
      (fn [evt] (store/destroy-todo (:id evt)))
    [:label :mouse-double-click]
      (fn [evt]
        (doto component
          (show/assoc!
            :text (show/get-props component :text)
            :editing true)))
    [:.edit :form-change]
      (fn [evt] (show/assoc! component :text (:value evt)))
    [:.edit :keyboard-up :keypress-enter]
      (fn [evt]
        (if (not= (:value evt) "")
          (store/update-todo (:value evt) (:id evt)))
        (show/assoc! component :editing false))))

(show/defclass TodoItem [component]
  (initial-state
    {:editing false
     :text (or (show/get-props component :text) "")})
  (render [props state]
    (let [wire (w/lay (item-wire-tap component) nil {:id (:id props)})]
      (dom/li {:key (:id props)
               :className (show/class-map {"completed" (:complete props)
                                           "editing"   (:editing state)})}
        (dom/div {:className "view"}
          (wired/input wire {:className "toggle"
                             :type "checkbox"
                             :checked (:complete props)})
          (wired/label wire (:text props))
          (wired/button wire {:className "destroy"}))
        (wired/input wire {:key ""
                           :className "edit"
                           :autoFocus true
                           :value (:text state)})))))

;; List
;;
(show/defclass TodoList [component]
  (render [props state]
    (dom/ul {:id "todo-list"}
      (map #(TodoItem %) (:todos props)))))

;; Main
;;
(defn main-wire [component]
  (w/taps (w/wire)
    :mouse-click
      (fn [evt] (store/toggle-complete-all))
    :form-change
      (fn [evt] (show/assoc! component :input (:value evt)))
    ))

(show/defclass Main [component]
  (initial-state
    {:input ""})
  (render [props state]
    (if (= 0 (count (:todos props)))
      (dom/noscript)
      (dom/section {:id "main"}
        (wired/input (main-wire component)
                     {:key "todo-input"
                      :id "toggle-all"
                      :type "checkbox"
                      :checked (:all-complete props)})
        (dom/label {:htmlFor "toggle-all"} "Mark all as complete")
        (TodoList props)))))


;; Footer
;;
(defn footer-wire []
  (w/tap (w/wire)
    :mouse-click
    (fn [evt] (store/clear-completed))))

(defn footer-with-todos [todos]
  (let [remaining-ct (count (filter (complement :complete) todos))
        completed-ct (- (count todos) remaining-ct)
        item-phrase (if (= 1 remaining-ct) "item" "items")]
    (dom/footer {:id "footer"}
      (dom/span {:id "todo-count"
                 :key "footer-span"}
                (dom/strong {:key "remaining-ct"} remaining-ct)
                (str " " item-phrase " left"))
      (let [curr-hash (.-hash js/location)
            states [[#{"#/"} "All"]
                    [#{"#/active"} "Active"]
                    [#{"#/completed"} "Completed"]]]
        (dom/ul {:id "filters"
                 :key "state-list"}
          (map
            (fn [[href n]]
              (dom/li {:key n}
                (dom/a {:key n
                        :className (show/class-map {"selected" (href curr-hash)})
                        :href (first href)} n)))
            states)))
      (if (> completed-ct 0)
        (wired/button (footer-wire)
                      {:id "clear-completed"
                       :key "clear-completed"}
                      "Clear completed (" completed-ct ")")))))

(show/defclass Footer [component]
  (render [props state]
    (footer-with-todos (:todos props))))

;; App
;;
(show/defclass App [component]
  (will-mount
    (aset js/window "awesome" component)
    (show/assoc! component :todos (store/all-todos))
    (store/register-changes
      (fn []
        (show/assoc! component
                     :todos (store/all-todos)
                     :all-complete (store/all-complete?)))))

  (render [props state]
    (dom/div
      (Header {:key "header"})
      (Main state)
      (Footer state))))

;; Super simple page routing
(doto (History.)
  (events/listen
    EventType/NAVIGATE
    (fn [e] (store/update-filter (.-token e))))
  (.setEnabled true))

;; Page Render
;;
(show/render-component
  (App)
  (.getElementById js/document "todoapp"))
