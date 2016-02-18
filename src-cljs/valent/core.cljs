(ns valent.core
  (:require
    [taoensso.timbre :as timbre
     :refer-macros (spy)]))

(defn state [atm])

(declare root*)

(defn by [k v data]
  "Given a collection of maps, returns exactly one map
   where the value at `key` matches `val`."

  (first (filter #(= (get % k) v) data)))

(defn process-map
  [qry state joins korks root-state]
  "Takes the current state map and selects out the desired
   keys, transforming with further subselectors when specified
  by the query."

  (letfn [(process-kv [m k v]
            (let
              [korks' (conj korks k)
               join (get joins korks')]
              (cond
                join
                (assoc m k
                       (root* v
                              (join root-state (get state k))
                              joins korks' root-state))

                (or (vector? v) (map? v))
                (assoc m k
                       (root* v (get state k) joins korks' root-state))

                :default
                (assoc m k (get state k))
                )))]
    (reduce-kv process-kv (empty qry) qry)))

(defn process-vector
  [qry state joins korks root-state]
  "Takes the current state vector and returns a vector of the
   desired selectored data."

  (let [subqry (first qry)
        korks' (conj korks 0)]
    (mapv #(root* subqry % joins korks' root-state) state)))

(defn root*
  [qry state joins korks root-state]
  (cond
    (map? qry)
    (process-map qry state joins korks root-state)

    (vector? qry)
    (process-vector qry state joins korks root-state)))

(defn root
  [qry state joins]
  (root* qry state joins [] state))
