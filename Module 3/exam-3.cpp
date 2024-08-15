#include <stdlib.h>
#include <assert.h>
#include <stdio.h>
#include <string>
#include <vector>
#include <stdint.h>
#include <algorithm>
#include <iostream>


struct Automaton {
    int m;
    size_t n, starting;
    std::vector<bool> is_final_state;
    std::vector<std::vector<int>> transitions;

    explicit Automaton(int m): m(m){}

    void readMe() {
        std::cin >> n >> starting;
        is_final_state.assign(n, false);
        transitions.resize(n);
        for (size_t i = 0; i < n; i++) {
            transitions[i].resize(m);
            std::string is_final;
            std::cin >> is_final;
            if (is_final == "+")
                is_final_state[i] = true;
            else
                assert(is_final == "-");
            for (int j = 0; j < m; j++)
                std::cin >> transitions[i][j];
        }
    }

    void longestWord() {
        std::vector<bool> final_reached(n, false);
        {
            std::vector<std::vector<uint64_t>> reverse_graph(n);
            for (uint64_t i = 0; i < n; i++) {
                for (uint64_t j = 0; j < m; j++) {
                    reverse_graph[transitions[i][j]].push_back(i);
                }
            }
            std::vector<uint64_t> detour;
            for (uint64_t i = 0; i < n; i++) {
                if (is_final_state[i]) {
                    detour.push_back(i);
                    final_reached[i] = true;
                }
            }
            while (!detour.empty()) {
                uint64_t v = detour.back(); detour.pop_back();
                for (uint64_t u: reverse_graph[v]) {
                    if (!final_reached[u]) {
                        final_reached[u] = true;
                        detour.push_back(v);
                    }
                }
            }
        }
        std::vector<bool> start_reachable(n, false);
        {
            std::vector<uint64_t> detour = {starting};
            start_reachable[starting] = true;
            while (!detour.empty()) {
                uint64_t v = detour.back(); detour.pop_back();
                for (int j = 0; j < m; j++) {
                    uint64_t u = transitions[v][j];
                    if (!start_reachable[u]) {
                        start_reachable[u] = true;
                        detour.push_back(u);
                    }
                }
            }
        }
        {
            std::vector<uint64_t> refs(n, 0);
            std::vector<uint64_t> sat_refs(n, 0);
            for (uint64_t i = 0; i < n; i++) {
                if (!start_reachable[i])
                    continue;
                for (uint64_t j = 0; j < m; j++) {
                    refs[transitions[i][j]]++;
                }
            }
            refs[starting]++;
            sat_refs[starting]++;

            struct PrevReturn {
                int ch;
                uint64_t v;
                PrevReturn(int ch, uint64_t v): ch(ch), v(v) {}
            };

            std::vector<PrevReturn> previos(n, PrevReturn(1337, 9999));
            std::vector<uint64_t> depth(n, 0);
            std::vector<uint64_t> todo = {starting};
            while (!todo.empty()) {
                uint64_t v = todo.back(); todo.pop_back();
                uint64_t depth_of_next = depth[v] + 1;
                for (int j = 0; j < m; j++) {
                    uint64_t u = transitions[v][j];
                    sat_refs[u]++;
                    if (sat_refs[u] == refs[u]) {
                        todo.push_back(u);
                    }
                    if (depth_of_next >= depth[u]) {
                        depth[u] = depth_of_next;
                        previos[u] = PrevReturn(j, v);
                    }
                }
            }
            bool reachable_sates_who_reach_final = false;
            bool reachable_cycles_who_reach_final = false;
            for (uint64_t v = 0; v < n; v++) {
                if (start_reachable[v] && final_reached[v]) {
                    reachable_sates_who_reach_final = true;
                    if (sat_refs[v] != refs[v]) {
                        reachable_cycles_who_reach_final = true;
                    }
                }
            }
            if (!reachable_sates_who_reach_final) {
                printf("EMPTY");
                return;
            }
            if (reachable_cycles_who_reach_final) {
                printf("INF");
                return;
            }
            uint64_t best_v = -57;
            uint64_t best_depth = 0;
            for (uint64_t i = 0; i < n; i++) {
                if (start_reachable[i] && is_final_state[i] && (sat_refs[i] == refs[i])) {
                    if (best_depth <= depth[i]) {
                        best_depth = depth[i];
                        best_v = i;
                    }
                }
            }
            std::string res;
            uint64_t cur = best_v;
            while (cur != starting) {
                res += static_cast<char>('a' + previos[cur].ch);
                cur = previos[cur].v;
            }
            std::reverse(res.begin(), res.end());
            printf("%lu\n", res.size());
        }
    }
};

int main() {
    int m;
    scanf("%d", &m);
    Automaton fa(m);
    fa.readMe();
    fa.longestWord();
    return 0;
}
