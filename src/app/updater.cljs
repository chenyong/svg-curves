
(ns app.updater (:require [respo.cursor :refer [mutate]]))

(defn updater [store op op-data]
  (case op
    :states (update store :states (mutate op-data))
    :drag-target (assoc store :drag-target op-data)
    :move (assoc store (:drag-target store) op-data)
    store))
