(ns ring-app.uploader
  (:require
   [clojure.java.io :as io])
  ;;[ring.util.response :refer [redirect file-response]]
  (:import [java.io File FileInputStream FileOutputStream]
           [java.util.zip ZipInputStream]))

;; This file deals with the uploading of files (images) to the server as well as serving them.

;; 3 defined paths don't look too well. There is no need to store this data.
;; TODO: optimize this by less definitions.

(def training-data "dataset.zip")

(def root-folder ".\\uploadedData\\")

(def avatar-path ".\\uploadedData\\userPic\\")

(def training-path ".\\uploadedData\\training\\")

(def ML-file ".\\uploadedData\\recognizer\\trainingData.yml") ;; we assume that this file always exists, as we do always training before accessing it.

(run! #(if-not (.exists (io/file %))
         (do
           (println "Creating directories...")
           (.mkdir (io/file %)))) [root-folder avatar-path training-path])

(defn file-path [path & [filename]]
  (java.net.URLDecoder/decode
   (str path File/separator filename)
   "utf-8"))

(defn upload-file
  "uploads a file to the target folder"
  [path {:keys [tempfile size filename]}]
  (with-open [in (io/input-stream tempfile)
              out (io/output-stream (file-path path filename))]
    (io/copy in out)))

(defmacro while-let
  "The composition of a side-effects based while, and a single-binding let, ala if-let.\n\nAvoids loop/recur redundance."
  [[sym expr] & body]
  `(loop [~sym ~expr]
     (when ~sym
       ~@body
       (recur ~expr))))

(defn unzip-file [[path file]]
  "Input path and a file as one array."
  (with-open [zis (ZipInputStream. (io/input-stream (str path file)))]
    (while-let [entry (.getNextEntry zis)]
      (let [size (.getSize entry)
            bytes (byte-array size) ; XXX there should be a max size
            output (FileOutputStream. (str path (last (clojure.string/split (.getName entry) #"/"))))]
        (println (.getName entry))
        (.read zis bytes 0 size) ; mutate bytes
        (doto output (.write bytes) .close)))))

(defn unpack-and-remove []
  "Unpack training data and remove .zip afterwards"
  (if (.exists (io/file (str training-path training-data)))
    (do
      (unzip-file [training-path training-data])
      (if (.exists (io/file (str training-path training-data)))
        (io/delete-file ".\\uploadedData\\training\\dataset.zip"))))) ;;for some reason trainingdata and path didn't work

;;(io/delete-file (str training-path training-data))

