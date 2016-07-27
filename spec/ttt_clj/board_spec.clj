(ns ttt-clj.board-spec
  (:require [speclj.core :refer :all]
            [ttt-clj.board :refer :all]
            [clojure.set :as set]))

(describe "new-board"
  (it "returns a vector"
    (should (vector? (new-board))))
  (it "has length equal to board-length"
    (should= (count (new-board)) board-length))
  (it "has only empty_spot as its elements"
    (should (every? #(= empty_spot %) (new-board)))))

(describe "move"
  (with _ empty_spot)
  (it "sets a value to a given position when board is empty"
    (should= [@_ @_ @_
              @_ @_ :x
              @_ @_ @_] (move (new-board) 5 :x)))
  (it "sets a value to a given position even if board is not empty"
    (should= [@_ @_ @_
              @_ :o :x
              @_ @_ @_] (move [@_ @_ @_
                               @_ @_ :x
                               @_ @_ @_] 4 :o))))

(describe "is-board-empty?"
  (with _ empty_spot)
  (it "returns true if board has only empty spots"
    (should (is-board-empty? [@_ @_ @_ @_ @_ @_ @_ @_ @_])))
  (it "returns false if board has any spot with a marker"
    (should-not (is-board-empty? [@_ @_ @_ @_ :x @_ @_ @_ @_]))))

(describe "is-board-full?"
  (with _ empty_spot)
  (it "returns false if there is any empty_spot"
    (should-not (is-board-full? [:x :x :x :x :x :x :x :x @_])))
  (it "returns true if there is no empty_spots"
    (should (is-board-full? [:o :x :x :x :o :o :x :x :o]))))

(describe "is-spot-available?"
  (with _ empty_spot)
  (it "returns true if index is in range and board is empty"
    (should (is-spot-available? (new-board) 1)))
  (it "returns true if index has an empty spot"
    (should (is-spot-available? [@_ @_ @_
                                 @_ :x :x
                                 :o :o @_] 1)))
  (it "returns false is index has a marker"
    (should-not (is-spot-available? [@_ @_ @_
                                     @_ :x :x
                                     :o :o @_] 4))))

(describe "available-spots"
  (with _ empty_spot)
  (context "returns the indexes of all empty spots on board"
    (it "returns an empty collection if there is no spot available"
      (should= [] (available-spots (vec (repeat board-length :x)))))
    (it "returns [0] if only 0 is an empty spot"
      (should= [0] (available-spots [@_ :x :x :x :x :x :x :x :x :x])))
    (it "returns [0 2] if only those indexes have empty spots"
      (should= [0 2] (available-spots [@_ :x @_ :x :x :x :x :x :x])))))

(describe "is-valid-move?"
  (with _ empty_spot)
  (it "returns false if position is taken"
    (should-not (is-valid-move? [:x :x :x :x :x :x :x :x :x] 0)))
  (it "returns true if position has an empty_spot"
    (should (is-valid-move? [@_ :x :x :x :x :x :x :x] 0)))
  (it "returns false if position is smaller than zero"
    (should-not (is-valid-move? (new-board) -1)))
  (it "returns false if position is bigger than board length"
    (should-not (is-valid-move? (new-board) (inc board-length)))))

(describe "board-rows"
  (it "returns a collection that contains [0 1 2]"
    (should (set/subset? #{[0 1 2]} (set (board-rows)))))
  (it "returns a collection that contains [3 4 5]"
    (should (set/subset? #{[3 4 5]} (set (board-rows)))))
  (it "returns a collection that contains [6 7 8]"
    (should (set/subset? #{[6 7 8]} (set (board-rows))))))

(describe "board-columns"
  (it "returns a collection that contains [0 3 6]"
    (should (set/subset? #{[0 3 6]} (set (board-columns)))))
  (it "returns a collection that contains [1 4 7]"
    (should (set/subset? #{[1 4 7]} (set (board-columns)))))
  (it "returns a collection that contains [2 5 8]"
    (should (set/subset? #{[2 5 8]} (set (board-columns))))))

(describe "diagonal-forward"
  (it "returns indexes of diagonal forward"
    (should= [0 4 8] (diagonal-forward))))

(describe "diagonal-backward"
  (it "retuns indexes of the diagonal backward"
    (should= [2 4 6] (diagonal-backward))))

(describe "board-diagonals"
  (it "returns a collection that contains [0 4 8]"
    (should (set/subset? #{[0 4 8]} (set (board-diagonals)))))
  (it "returns a collection that contains [6 7 8]"
    (should (set/subset? #{[2 4 6]} (set (board-diagonals)))))
  (it "has only two elements"
    (should= 2 (count (board-diagonals)))))

(describe "winning-positions"
  (it "returns a collection that contains board rows"
    (should (set/subset? #{[0 1 2] [3 4 5] [6 7 8]}
                         (set (winning-positions)))))
  (it "returns a collection that contains board columns"
    (should (set/subset? #{[0 3 6] [1 4 7] [2 5 8]}
                          (set (winning-positions)))))
  (it "returns a collection that contains board diagonals"
    (should (set/subset? #{[0 4 8] [2 4 6]} (set (winning-positions))))))

(describe "repeated-markers?"
  (with _ empty_spot)
    (it "returns false if there is no repeated markers on given positions"
      (should-not (repeated-markers? [:x :o :x
                                      @_ :x :o
                                      :o :x :o] [0 1 2])))
    (it "returns false if it finds repeated empty spots given positions"
      (should-not (repeated-markers? [@_ @_ @_
                                     :x :o :x
                                     :o :x :o] [0 1 2])))
    (it "returns true if it finds repeated markers in a row"
      (should (repeated-markers? [:o :x :o
                                  :x :x :x
                                  :o :o :x] [3 4 5])))
    (it "returns true if it finds repeated markers in a column"
      (should (repeated-markers? [:o :x :x
                                  :o :x :x
                                  :o @_ @_] [0 3 6])))
    (it "returns true if it finds repeated markers in a diagonal"
      (should (repeated-markers? [:o :x :x
                                  :x :o @_
                                  @_ @_ :o] [0 4 8]))))

(describe "find-repetition"
  (with _ empty_spot)
  (context "finds a collection with winning positions corresponding to repeated markers on board"
    (it "returns a board row if it has repeated markers"
      (should= [[0 1 2]] (find-repetition [:x :x :x
                                           @_ :o @_
                                           :o @_ @_])))
    (it "returns a board column if it has repeated markers"
      (should= [[1 4 7]] (find-repetition [@_ :o :x
                                           :x :o :x
                                           :x :o @_])))
    (it "returns a board diagonal if it has repeated markers"
      (should= [[2 4 6]] (find-repetition [@_ :o :x
                                           :o :x @_
                                           :x @_ @_])))
    (it "returns more than combo if there are more than one repeated positions"
      (should (set/subset? #{[0 4 8] [3 4 5]}
                           (set (find-repetition [:x :o :o
                                                  :x :x :x
                                                  :o :o :x])))))))

(describe "winning-combo"
  (with _ empty_spot)
  (with repeated-combos [:x :o :o
                         :x :x :x
                         :o :o :x])
  (it "returns the combo with repeated markers"
    (should= [0 1 2] (winning-combo [:x :x :x
                                     @_ :o @_
                                     :o @_ @_])))
  (it "returns only one combo even if there are more than one with repeated markers"
    (should (or (= [0 4 8] (winning-combo @repeated-combos))
                (= [3 4 5] (winning-combo @repeated-combos))))))
