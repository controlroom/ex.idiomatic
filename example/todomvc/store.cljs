(ns todomvc.store)

(defn todo [text]
  {:id (gensym)
   :complete false
   :text text})

(def todos
  (let [t1 (assoc (todo "Create a TodoMVC template") :complete true)
        t2 (todo "Rule the Web")]
    (atom {(:id t1) t1
           (:id t2) t2})))

(def callbacks   (atom []))
(def todo-filter (atom nil))

(defn all-todos []
  (filter @todo-filter (vals @todos)))

(defn all-complete? []
  (every? :complete (vals @todos)))

(defn register-changes [f]
  (swap! callbacks conj f))

(defn emit-change []
  (doseq [cb @callbacks]
    (cb)))

(defn update-filter [current-filter]
  (let [filter-map {"/active" (complement :complete)
                    "/completed" :complete
                    "/" identity}]
    (reset! todo-filter (get filter-map current-filter identity))
    (emit-change)))

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
