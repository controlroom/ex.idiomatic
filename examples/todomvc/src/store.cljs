(ns examples.todomvc.store)

(def todos (atom {}))
(def callbacks (atom []))

(defn all-todos []
  (vals @todos))

(defn all-complete? []
  (every? :complete (vals @todos)))

(defn todo [text]
  {:id (gensym)
   :complete false
   :text text})

(defn register-changes [f]
  (swap! callbacks conj f))

(defn emit-change []
  (doseq [cb @callbacks]
    (cb)))

(defn create-todo [text]
  (let [todo (todo text)]
    (swap! todos (fn [old] (assoc old (:id todo) todo)))
    (emit-change)))

(defn destroy-todo [id]
  (swap! todos (fn [old] (dissoc old id)))
  (emit-change))

(defn toggle-complete [id]
  (swap! todos (fn [old] (update-in old [id :complete] not)))
  (emit-change))

(defn update-todo [text id]
  (swap! todos (fn [old] (assoc-in old [id :text] text)))
  (emit-change))

(defn clear-completed []
  (swap! todos (fn [old] (into {} (filter #(not (:complete (second %))) old))))
  (emit-change))

(defn toggle-complete-all []
  (swap! todos (fn [old]
                 (let [toggle (not (all-complete?))]
                   (into {} (for [[k v] old] [k (assoc v :complete toggle)])))))
  (emit-change))
