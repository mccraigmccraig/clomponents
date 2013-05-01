(ns clomponents.control-test
  (:use midje.sweet
        clomponents.control)
  (:require [clojure.tools.logging :as log]))

(fact "resolving a fn with package qualified name"
  (resolve-fn {:foo 'clojure.core/identity} :foo) => #'identity)

(fact "resolving a fn with unqualified name and default namespace"
  (resolve-fn {:ns 'clojure.core :foo 'identity} :foo) => #'identity)

(fact "resolving a fn with default namespace and default name"
  (resolve-fn {:ns 'clojure.core} :identity) => #'identity)

(fact "resolving a literal function"
  ((resolve-fn {:foo (fn [foo] (* 2 foo))} :foo) 10) => 20)

(fact "failing to resolve a required function"
  (resolve-fn {} :foo) => (throws #"could not resolve"))

(fact "failing to resolve a non-required function"
  (resolve-fn {} :foo :required? false) => nil)

(fact "clomponent lifecycle"
  (let [c (fn [config] (fact (:id config) => ..id..) ..obj..)
        d (fn [config obj] (fact (:id config) => ..id.. obj => ..obj..) :destroy)
        cl (create-namespace-clomponent {:create c :destroy d :id ..id..})]

    (create cl) => ..obj..
    (object cl) => ..obj..

    (destroy cl) => :destroy
    @(:obj cl) => nil
    (object cl) => ..obj..))

(fact "pass additional-config to create"
  (let [c (fn [config] (fact (:id config) => ..id.. (:additional-data config) => ..additional..) ..obj..)
        cl (create-namespace-clomponent {:id ..id.. :create c})]

    (create cl {:additional-data ..additional..}) => ..obj..
    (object cl) => ..obj..))

(fact "no destroy function"
  (let [c (fn [config] (fact (:id config) => ..id..) ..obj..)
        cl (create-namespace-clomponent {:create c :id ..id..})]

    (create cl) => ..obj..
    (object cl) => ..obj..

    (destroy cl) => nil
    @(:obj cl) => nil
    (object cl) => ..obj..))

(fact "if destroy function borks"
  (let [c (fn [config] (fact (:id config) => ..id..) ..obj..)
        d (fn [config obj] (fact (:id config) => ..id.. obj => ..obj..) (throw (RuntimeException. "bork!")))
        cl (create-namespace-clomponent {:create c :destroy d :id ..id..})]

    (create cl) => ..obj..
    (object cl) => ..obj..

    (destroy cl) => nil
    (provided
      (log/log* anything :warn anything anything) => nil)

    @(:obj cl) => nil
    (object cl) => ..obj..))

(fact "perform an arbitrary action"
  (let [c (fn [config] (fact (:id config) => ..id..) ..obj..)
        f (fn [config obj] (fact (:id config) => ..id.. obj => ..obj..) ..fooblerise..)
        cl (create-namespace-clomponent {:id ..id.. :create c :fooblerise f})]

    (create cl) => ..obj..
    (object cl) => ..obj..

    (perform cl :fooblerise) => ..fooblerise..))
