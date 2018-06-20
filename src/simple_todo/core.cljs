(ns simple-todo.core
  (:require [show.core :as show]
            [show.dom  :as dom]
            [wire.up.show :as wired]
            [wire.core :as w]))

(defn create-todo [text]
  {:id (str (gensym)), :text text})

(defn tap-input [wire component]
  (w/tap wire [:keyboard-up :keypress-enter]
    (fn [e]
      (w/act wire :new-todo {:text (:value e)})
      (set! (.-value (:target e)) ""))))

(show/defclass TodoInput [component]
  (render [{:keys [wire]} state]
    (let [tapped-wire (tap-input wire component)]
      (dom/div {:className "input-container"}
        (wired/input tapped-wire)))))

(defn tap-item [wire component]
  (w/taps wire
    :mouse-double-click #(w/act wire :remove-todo)
    :mouse-click        #(show/update-in! component [:selected] not)))

(show/defclass Todo [component]
  (initial-state []
    {:selected false})
  (render [{:keys [text wire]}
           {:keys [selected]}]
    (wired/li (tap-item wire component)
              {:className {"list-item" true, "selected" selected}}
              text)))

(show/defclass TodoList [component]
  (render [{:keys [todos wire]} state]
    (dom/ul {:className "list"}
      (show/transition-group
        (show/css-transition {:key "list-item"}
          (dom/div
            (map #(Todo {:key  (:id %)
                         :wire (w/lay wire nil {:id (:id %)})
                         :text (:text %)})
                 todos)))))))

(defn init-todos-wire [component]
  (w/taps (w/wire)
    :new-todo    #(show/update-in! component [:todos]
                   (fn [todos] (conj todos (create-todo (:text %)))))
    :remove-todo #(show/update-in! component [:todos]
                   (fn [todos] (remove (fn [todo] (= (:id todo) (:id %))) todos)))))

(show/defclass Todos [component]
  (initial-state []
    {:local-wire (init-todos-wire component)
     :todos      (into () (show/get-props component :todos))})
  (render [props {:as state :keys [local-wire todos]}]
    (dom/div {:className "todos-container"}
      (TodoInput {:wire local-wire})
      (dom/div {:className "list-container"}
        (TodoList {:wire  local-wire
                   :todos todos})))))

(show/render-component
  (Todos {:todos [(create-todo "Stuff") (create-todo "More stuff")]})
  (.getElementById js/document "app"))
