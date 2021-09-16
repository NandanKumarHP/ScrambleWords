(ns scramble-strings.core-test
  (:require [clojure.test :refer :all]
            [scramble-strings.core :refer :all]))

(deftest check-scramble-for-words
     (testing "return true if all characters in the second word is present in the first word with lowercase letters"
        (is (= true (is-scramble? "rekqodlw" "world")))
        (is (= true (is-scramble? "cedewaraaossoqqyt" "codewars"))))
     (testing "return true if all characters in the second word is present in the first word with upper and lower case letters"
      (is (= true (is-scramble? "rekQODlw" "world")))
      (is (= true (is-scramble? "CedewaRAAOssoqqYT" "codewars"))))
     (testing "return false if all the characters in the second word is not present in the first word"
        (is (= false (is-scramble? "katas" "steak")))
        (is (= false (is-scramble? "katas" "steak"))))
     (testing "return false always if either of the words contains a number or special character"
      (is (= false (is-scramble? "r12ekqodlw" "world")))
      (is (= false (is-scramble? "rekqod2346lw" "worl23d")))
      (is (= false (is-scramble? "cloj@re" "jre")))
      (is (= false (is-scramble? "qusino#$@ntesinfjjasujsn" "#$@ntesinf")))))
