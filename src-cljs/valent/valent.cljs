(ns valent.)

(defn state [atm])

(defn apply-value-fn [value-fn dv joins]
  (cond
    (:--joiner-name value-fn)
    ((->> (:--joiner-name value-fn)
          (get joins))
     dv)

    :default (value-fn dv)))

(defn qry [q data joins]
  (letfn [(apply-value-fn [m dk dv]
            (let [value-fn (get q dk)]
              (assoc m dk (apply-value-fn value-fn dv joins))))]
    (let [ks (keys q)
          subset (select-keys data ks)]
      (reduce-kv apply-value-fn {} subset))))

(defn by [key val data]
  (first (filter #(= (get data key) val))))

(defn join [joiner]
  {:--joiner-name joiner})

(defn build [m]
  (fn [data joins]
    (qry m data joins)))

(defn process-map [qry state joins]
  (let [ks (keys qry)]
    ""))

(defn root
  [qry state joins]
  (cond
    (associative? qry)
    (process-map qry state joins)

    (vector? qry)
    (comment "Handle query vectors")))
