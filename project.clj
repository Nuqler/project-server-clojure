(defproject ring-app "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [metosin/muuntaja "0.6.4"]
                 [metosin/reitit "0.3.9"]
                 [metosin/ring-http-response "0.9.1"]
                 [ring "1.7.1"]
                 [com.layerware/hugsql "0.4.9"]
                 [org.clojure/java.jdbc "0.7.9"]
                 [com.h2database/h2 "1.4.200"]
                 [org.postgresql/postgresql "42.2.6"]
                 [luminus-migrations "0.6.6"]]
  :repl-options {:init-ns ring-app.core}
  :main ring-app.core
  :profiles
  {:uberjar {:omit-source true
             :aot :all}})
