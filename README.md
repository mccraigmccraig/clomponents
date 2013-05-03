# clomponents

[![Build Status](https://secure.travis-ci.org/mccraigmccraig/clomponents.png)](http://travis-ci.org/mccraigmccraig/clomponents)


## Stupid simple component management for clojure apps

a component is some resource with a create/destroy lifecycle. clomponents gives you a registry of components, and
a way of defining functions to create/destroy/do-other-things to the components

each component is defined by a config hash. there must at least be a create function for the component, which takes a single argument (the config)

a destroy function may also be defined, which takes two arguments : the config and the object, and further arbitrary action functions may be defined, which will also take the same two arguments as destroy : the config and object

functions can be found in several ways :

* namespace qualified reference : `{:create 'foo/create-me}`
* default namespace and unqualified reference : `{:ns 'foo :create 'create-me}`
* default name in namespace : `{:ns 'foo}` will use `'foo/create` for creation and `'foo/destroy` for destruction
* literal function : `{:create (fn [config] "do something"}`

## Usage

add the dependency to your project.clj

    [clomponents "0.4.0"]

define some components

    ;; define some components
    (def comps {:foo {:ns 'foo}

                :bar {:create 'bar/create-me :destroy 'bar/destroy-me :meep 'bar/meep-me}

                :baz {:create (fn [config] {:baz config})
                      :bloop (fn [config obj] (prn "bloop!") (prn config) (prn obj))}

create a registry and manage the components

    (require '[clomponents.core :as clom])

    (def registry (clom/create-registry comps))

    ;; instantiate components
    (create registry :foo)
    (create registry :bar)
    (create registry :baz)

    ;; retrieve component objects
    (object registry :foo)

    ;; do some arbitrary actions
    (perform registry :bar :meep)
    (perform registry :baz :bloop)

    ;; tear down the components
    (destroy registry :foo)
    (destroy registry :bar)
    (destroy registry :baz)


## License

Copyright © 2013 mccraigmccraig of the clan mccraig

Distributed under the Eclipse Public License, the same as Clojure.
