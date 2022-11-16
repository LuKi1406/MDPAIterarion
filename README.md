# MDPAIterarion
Find fastest way and avoid opsticals, best policy with MDP

Train agent through iteration using MDP formula to find best policy, avoid obstacles and find fastest way, train agent and punish him, count how many times agent has been punished. 
Very interesting implementation MDP with Java, better reward(higher) is the better way to destination inside table with columns and rows, + is our destination, @ obstacle and - punishment. 

Combination of probability, max index, possibility and final result which is already decided within Constants class.

Interesting to watch how computer can learn which is the best way, best path to actual destination.
Added threads for fun, punishing agent with slowing it down if he wants to go to a state that is not good and count how many times he did that with creating thread for each punish.
