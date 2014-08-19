(ns clomponents.core-test
  (:use midje.sweet
        clomponents.core)
  (:require [clojure.tools.logging :as log]
            [clomponents.control :as control]))

(fact "create-registry should return a hash of clomponents"
  (let [config {:foo {:id ..idfoo..
                      :create (fn [c] (fact (:id c) => ..idfoo..) ..fooobj..)
                      :fibblerise (fn [c o] (fact (:id c) => ..idfoo.. o => ..fooobj..) ..fibblerise..)}
                :bar {:id ..idbar..
                      :create (fn [c] (fact (:id c) => ..idbar.. (:additional-data c) => ..additional..) ..barobj..)
                      :destroy (fn [c o] (fact (:id c) => ..idbar.. o => ..barobj..) ..destroy-bar..)}}
        r (create-registry config)]

    (control/create (:foo r)) => ..fooobj..
    (control/object (:foo r)) => ..fooobj..
    (control/perform (:foo r) :fibblerise) => ..fibblerise..
    (control/destroy (:foo r)) => nil

    (control/create (:bar r) {:additional-data ..additional..}) => ..barobj..
    (control/object (:bar r)) => ..barobj..
    (control/destroy (:bar r)) => ..destroy-bar..))

;; TODO fix to test without using midje open-protocols which screw up AOT
;;
;; (fact "swap-registry should destroy the old registry clomponents before creating new"
;;       (let [config {:foo {:id ..idfoo..
;;                           :create (fn [c] (fact (:id c) => ..idfoo..) ..fooobj..)}
;;                     :bar {:id ..idbar..
;;                           :create (fn [c] (fact (:id c) => ..idbar..) ..barobj..)}
;;                     :baz {:id ..idbaz..
;;                           :create (fn [c] (fact (:id c) => ..idbaz..) ..bazobj..)}}
;;             ratom (atom (create-registry config))
;;             foo-clomp (@ratom :foo)
;;             bar-clomp (@ratom :bar)
;;             baz-clomp (@ratom :baz)]

;;         (control/create foo-clomp) => ..fooobj..
;;         (control/create bar-clomp) => ..barobj..

;;         (swap-registry ratom config) => anything
;;         (provided
;;          (control/destroy baz-clomp) => nil
;;          (control/destroy bar-clomp) => nil
;;          (control/destroy foo-clomp) => nil)

;;         (@ratom :foo) =not=> foo-clomp
;;         (control/create (:foo @ratom)) => ..fooobj..

;;         (@ratom :bar) =not=> bar-clomp
;;         (control/create (:bar @ratom)) => ..barobj..

;;         (@ratom :baz) =not=> baz-clomp
;;         (control/create (:baz @ratom)) => ..bazobj..))

(fact "swap-registry should do nothing if the old registry is null"
  (let [config {:foo {:id ..idfoo..
                      :create (fn [c] (fact (:id c) => ..idfoo..) ..fooobj..)}}
        ratom (atom nil)]

    (swap-registry ratom config) => anything

    (control/create (:foo @ratom)) => ..fooobj..))
