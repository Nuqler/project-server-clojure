(ns ring-app.python-shell
  (:require [clojure.java.shell :as shell]))

;;expand in future?
(defn train-recognizer []
  "Train recognizer with faces from the dataset"
  (shell/sh "python" "uploadedData\\TrainRecognizer.py"))
