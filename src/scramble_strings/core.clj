(ns scramble-strings.core
  (:use compojure.core)
  (:require [org.httpkit.server :as server]
    [compojure.core :refer :all]
    [compojure.route :as route]
    [clojure.data.json :as json]
    [ring.middleware.defaults :refer :all]
    [hiccup.core :as hiccup]
    [ring.middleware.params :refer :all]
    [clojure.string :as str]))

(defn check-scramble [word1 word2]
  (loop [n (count word2)
         first-word word1
         sec-word word2]
    (if (or (= n 0) (nil? n))
      true
      (if (clojure.string/includes? first-word (str (get sec-word 0)))
        (recur (- n 1)
               (str (subs first-word 0 (clojure.string/index-of first-word (str (get sec-word 0))))
                    (subs first-word (+ 1 (clojure.string/index-of first-word (str (get sec-word 0))))))
               (subs sec-word 1))
        false))))

(defn is-scramble? [string1 string2]
    (cond
      (nil? string2) false
      (nil? string1) false
      (> (count string2) (count string1)) false
      (nil? (re-matches #"^[a-z]+$" (str/lower-case string1))) false
      (nil? (re-matches #"^[a-z]+$" (str/lower-case string2))) false   
      (= string1 string2) true
      (check-scramble (str/lower-case string1) (str/lower-case string2)) true
      :else false
    )
  )

  (defn simple-body-page [req]
    {:status  200
     :headers {"Content-Type" "text/html"}
     :body    (hiccup/html
                [:head
                 [:title "Scramble Words Verify"]]
                  [:body
                    [:form {:action "/scramble" :method "GET"}
                      [:div {:style "margin:10px"} [:span "Enter First Word:"]
                        [:input {:type "text" :name "first" :value ""}]]
                      [:div {:style "margin:10px"} [:span "Enter First Word:"]
                        [:input {:type "text" :name "second" :value ""}]]
                      [:div
                        [:input {:type "Submit" :value "Submit"}]]]])})
  
  (defn request-example [req]
    (println  req)
  (let [first-word (get (:query-params req) "first")
        second-word (get (:query-params req) "second")]
       {:status  200
        :headers {"Content-Type" "application/json"}
        :body    (->
                   {:first-word first-word
                  :second-word second-word
                  :is-scrambled? (is-scramble? first-word second-word)}
                   json/write-str)}))

  (defroutes app-routes
    (GET "/" [] simple-body-page)
    (GET "/scramble" [] request-example)
    (route/not-found "Error, page not found!"))

(def app (-> app-routes
             wrap-params))

(defn -main
  "This is our main entry point"
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    (server/run-server (wrap-defaults #'app site-defaults) {:port port})
    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))
