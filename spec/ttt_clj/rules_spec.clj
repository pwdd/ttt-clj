(ns ttt-clj.rules-spec
  (:require [speclj.core :refer :all]
            [ttt-clj.rules :refer :all]
            [ttt-clj.board :as board]))

(describe "draw?"
  (with _ board/empty_spot)
  (it "returns false if board is empty"
    (should-not (draw? (board/new-board))))
  (it "returns false if board is not completely full"
    (should-not (draw? [:x :x @_
                        :o :o :x
                        :o :x :o])))
  (it "returns false if board is full and there is a winner"
    (should-not (draw? [:x :o :x
                        :x :x :o
                        :o :o :x])))
  (it "returns true if board is full and there is no winner"
    (should (draw? [:x :x :o
                    :o :o :x
                    :x :o :x]))))

(describe "game-over?"
  (with _ board/empty_spot)
  (it "returns false if board is empty"
    (should-not (game-over? (board/new-board))))
  (it "returns false if there is any empty spot on board"
    (should-not (game-over? [:x :x @_
                             :o :o :x
                             :o :x :o])))
  (it "returns true if game tied"
    (should (game-over? [:x :x :o
                         :o :o :x
                         :x :o :x])))
  (it "returns true if there is a winner"
    (should (game-over? [:x :x :x
                         :o :o @_
                         @_ @_ @_]))))

(describe "winner"
  (it "returns the marker that is in the winning positions"
    (should= :x (winner [:x :x :x
                         :o :o :x
                         :o :x :o]))))
