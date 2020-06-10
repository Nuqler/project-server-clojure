(ns ring-app.auth
  (:require
   [ring-app.db.core :as db]
   ;; --- TOKEN SYSTEM ---
   [buddy.core.nonce :as nonce]
   [buddy.core.codecs :as codecs]
   [buddy.auth :refer [authenticated? throw-unauthorized]]
   [buddy.auth.backends.token :refer [token-backend]]
   [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]))

(defn gen-random-token
  []
  (let [randomdata (nonce/random-bytes 16)]
    (codecs/bytes->hex randomdata)))

(def tokens (atom {}))

;; (defn login-get-token-test [login-details]
;;   (if-let [validated-user (db/login login-details)]
;;     (if-not (and (nil? validated-user) (contains? @tokens (keyword (:Email validated-user))))
;;       (let [token (gen-random-token)
;;             Email (:Email validated-user)]
;;         (swap! tokens assoc (keyword token) (keyword Email))
;;         (assoc-in validated-user [0 :token] token))))) ;;return user with toke

;; (defn validate-user
;;   [login-details]
;;   "First version of user validation"
;;   (let [Email (:Email login-details)
;;         Token (:Token login-details)]
;;     (if (and (contains? @tokens (keyword Email)) ())
;;       ))
;;   )

;; TODO: add token timeout.
(defn login-get-token-test2
  [login-details]
  """Generate a unique token for user to be used for authorization. Stored in 'tokens atom'.
     User email is used to check for existance of tokens.
     Wrapper function for login without a token."""
  (if-let [validated-user (db/login login-details)]
    (let [token (gen-random-token)
          Email (:Email login-details)]
      (if-not (contains? @tokens (keyword Email))
        (do
          (swap! tokens assoc (keyword Email) (keyword token))
          (assoc-in validated-user [0 :token] token))
        {:result "Error: User already logged in!"}))))

(defn logout-token-test
  [login-details]
  """Removes token from 'tokens atom' making further user's actions unauthorized."""
  (let [Email (:Email login-details)]
    (if (contains? @tokens (keyword Email))
      (do
        (swap! tokens dissoc (keyword Email))
        {:result "Successfuly logged out."})
      {:result "Error: user is not logged in."})))
