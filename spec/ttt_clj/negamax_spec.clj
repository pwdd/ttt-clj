(ns ttt-clj.negamax-spec
  (:require [speclj.core :refer :all]
            [ttt-clj.negamax :refer :all]
            [ttt-clj.board :as board]))

(describe "board-analysis"
  (it "returns 10 if current player won and -10 if opponent won"
    (should= 10 (board-analysis [:x :x :x
                                 :o :o :x
                                 :o :x :o] :x :o start-depth))
    (should= -10 (board-analysis [:x :x :x
                                  :o :o :x
                                  :o :x :o] :o :x start-depth)))
  (it "returns 8 if current-player won and -8 if opponent won"
    (should= 8 (board-analysis [:x :x :x
                                 :o :o :x
                                 :o :x :o] :x :o 2))
    (should= -8 (board-analysis [:x :x :x
                                 :o :o :x
                                 :o :x :o] :o :x 2)))
  (it "returns 0 if game ended in a draw"
    (should= 0 (board-analysis [:x :x :o
                                :o :o :x
                                :x :o :x] :x :o 2))))

(describe "max-score"
  (with _ board/empty_spot)
  (it "returns 0 if game will end in a tie"
    (should= 0 (max-score [:x :x :o
                           :o :o @_
                           :x :x :o] :x :o 2)))
  (it "returns 9 if current player will win the game"
    (should= 9 (max-score [:x :x @_
                           :o :o :x
                           :o :x :o] :x :o start-depth)))
  (it "returns -8 if opponent will win the game"
    (should= -8 (max-score [:o :x :o
                            :o :o :x
                            @_ :x @_] :x :o start-depth))))

(describe "best-move"
  (with _ board/empty_spot)
  (it "blocks opponent from winning"
    (should= 2 (best-move [:o :o @_
                           :x @_ @_
                           :x @_ @_] :x :o start-depth)))
  (it "wins when it has the chance"
    (should= 5 (best-move [:o :o @_
                           :x :x @_
                           @_ @_ @_] :x :o start-depth)))
  (it "avoids situation in which opponent can win in two positions"
    (should (or (= 2 (best-move [:x @_ @_
                                 @_ :o @_
                                 @_ @_ :o] :x :o start-depth))
                (= 6 (best-move [:x @_ @_
                                  @_ :o @_
                                  @_ @_ :o] :x :o start-depth))))))
