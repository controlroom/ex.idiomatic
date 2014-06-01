(ns examples.todomvc.app
  (:require [show.core :as show :include-macros true]
            [show.dom  :as dom  :include-macros true]
            [wire.up.show :as wired :include-macros true]
            [wire.core :as w]
            [examples.todomvc.store :as store]))

;; Header
;;
(defn header-wire []
  (w/taps (w/wire)
    {:key :keyboard-up :keypress :enter}
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
    {:key :toggle-complete :action :click}
      (fn [evt] (store/toggle-complete (:id evt)))
    {:key :destroy :action :click}
      (fn [evt] (store/destroy-todo (:id evt)))
    {:key :label :action :double-click}
      (fn [evt]
        (show/assoc! component :text (show/get-props component :text))
        (show/assoc! component :editing true))
    {:key :edit :action :change}
      (fn [evt] (show/assoc! component :text (:value evt)))
    {:key :edit :action :up :keypress :enter}
      (fn [evt]
        (if (not= (:value evt) "")
          (store/update-todo (:value evt) (:id evt)))
        (show/assoc! component :editing false))))

(show/defclass TodoItem [component]
  (initial-state
    {:editing false
     :text (or (show/get-props component :text) "")})
  (render [props state]
    (let [wire (item-wire-tap component)]
      (dom/li {:className (show/class-map {"completed" (:complete props)
                                           "editing"   (:editing state)})}
        (dom/div {:className "view"}
          (wired/input (w/lay wire :toggle-complete {:id (:id props)})
                       {:className "toggle"
                        :type "checkbox"
                        :checked (:complete props)})
          (wired/label (w/lay wire :label) (:text props))
          (wired/button (w/lay wire :destroy {:id (:id props)})
                        {:className "destroy"}))
        (wired/input (w/lay wire :edit {:id (:id props)})
                     {:className "edit"
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
(defn main-wire []
  (w/taps (w/wire)
    :mouse-click
    (fn [evt] (store/toggle-complete-all))))

(show/defclass Main [component]
  (render [props state]
    (if (= 0 (count (:todos props)))
      (dom/noscript)
      (dom/section {:id "main"}
        (wired/input (main-wire)
                     {:id "toggle-all"
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
      (dom/span {:id "todo-count"}
                (dom/strong remaining-ct)
                (str " " item-phrase " left"))
      (dom/ul {:id "filters"}
        (dom/li (dom/a {:className "selected", :href "#/"} "All"))
        (dom/li (dom/a {:href "#/active"} "Active"))
        (dom/li (dom/a {:href "#/completed"} "Completed")))
      (if (> completed-ct 0)
        (wired/button (footer-wire)
                      {:id "clear-completed"}
                      "Clear completed (" completed-ct ")")))))

(show/defclass Footer [component]
  (render [props state]
    (if (= 0 (count (:todos props)))
      (dom/noscript)
      (footer-with-todos (:todos props)))))

;; App
;;
(show/defclass App [component]
  (will-mount
    (store/register-changes
      (fn []
        (show/assoc! component :todos (store/all-todos))
        (show/assoc! component :all-complete (store/all-complete?)))))

  (render [props state]
    (dom/div
      (Header)
      (Main state)
      (Footer state))))

;; Page Render
;;
(show/render-component
  (App)
  (.getElementById js/document "todoapp"))
