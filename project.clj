(defproject ring-app "0.0.5a-SNAPSHOT"
  :description "Serverside"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [metosin/muuntaja "0.6.6"]
                 [metosin/reitit "0.4.2"]
                 [metosin/ring-http-response "0.9.1"]
                 [ring "1.8.1"]
                 [http-kit "2.3.0"]
                 [com.layerware/hugsql "0.5.1"]
                 [com.layerware/hugsql-adapter-next-jdbc "0.5.1"]
                 [com.microsoft.sqlserver/mssql-jdbc "8.2.2.jre11"]
                 [seancorfield/next.jdbc "1.0.424"]
                 [buddy "2.0.0"]]
  :plugins [[lein-ring "0.12.5"]]
  :repl-options {:init-ns ring-app.core}
  :main ring-app.core
  :ring {:handler ring-app.settings/handler
         :main ring-app.core}
  :jvm-opts ["-Duser.timezone=UTC"] ;; Important flag to make java.sql.Timestamp show correct timezone upon retrieval from DB.
  :nrepl {:start? false}
  :target-path "target/%s")
