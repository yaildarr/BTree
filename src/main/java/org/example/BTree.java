package org.example;


class BTree {
    private static final int T = 3; // Порядок B-дерева

    // Узел дерева
    private static class Node {
        int n; // Количество ключей
        int[] keys; // Ключи
        Node[] children; // Дочерние
        boolean isLeaf; // Листовой узел или нет

        Node(boolean isLeaf) {
            this.isLeaf = isLeaf;
            keys = new int[2 * T - 1];
            children = new Node[2 * T];
            n = 0;
        }
    }

    private Node root;

    public BTree() {
        root = new Node(true);
    }

    // Вставка ключа в дерево
    public void insert(int key) {
        Node r = root;
        if (r.n == 2 * T - 1) {
            Node s = new Node(false);
            s.children[0] = r;
            splitChild(s, 0);
            insertNonFull(s, key);
            root = s;
        } else {
            insertNonFull(r, key);
        }
    }

    private void insertNonFull(Node x, int k) {
        int i = x.n - 1;
        if (x.isLeaf) {
            while (i >= 0 && k < x.keys[i]) {
                x.keys[i + 1] = x.keys[i];
                i--;
            }
            x.keys[i + 1] = k;
            x.n++;
        } else {
            while (i >= 0 && k < x.keys[i]) {
                i--;
            }
            i++;
            if (x.children[i].n == 2 * T - 1) {
                splitChild(x, i);
                if (k > x.keys[i]) {
                    i++;
                }
            }
            insertNonFull(x.children[i], k);
        }
    }

    private void splitChild(Node x, int i) {
        Node y = x.children[i];
        Node z = new Node(y.isLeaf);
        z.n = T - 1;

        for (int j = 0; j < T - 1; j++) {
            z.keys[j] = y.keys[j + T];
        }
        if (!y.isLeaf) {
            for (int j = 0; j < T; j++) {
                z.children[j] = y.children[j + T];
            }
        }
        y.n = T - 1;

        for (int j = x.n; j >= i + 1; j--) {
            x.children[j + 1] = x.children[j];
        }
        x.children[i + 1] = z;

        for (int j = x.n - 1; j >= i; j--) {
            x.keys[j + 1] = x.keys[j];
        }
        x.keys[i] = y.keys[T - 1];
        x.n++;
    }

    // Поиск ключа в дереве
    public boolean search(int key) {
        return search(root, key);
    }

    private boolean search(Node x, int key) {
        int i = 0;
        while (i < x.n && key > x.keys[i]) {
            i++;
        }
        if (i < x.n && key == x.keys[i]) {
            return true;
        } else if (x.isLeaf) {
            return false;
        } else {
            return search(x.children[i], key);
        }
    }

    // Удаление ключа из дерева
    public void delete(int key) {
        delete(root, key);
        if (root.n == 0) {
            if (!root.isLeaf) {
                root = root.children[0];
            }
        }
    }

    private void delete(Node x, int key) {
        int i = 0;
        while (i < x.n && key > x.keys[i]) {
            i++;
        }

        if (i < x.n && key == x.keys[i]) {
            if (x.isLeaf) {
                for (int j = i; j < x.n - 1; j++) {
                    x.keys[j] = x.keys[j + 1];
                }
                x.n--;
            } else {
                Node leftChild = x.children[i];
                Node rightChild = x.children[i + 1];

                if (leftChild.n >= T) {
                    x.keys[i] = deletePredecessor(leftChild);
                } else if (rightChild.n >= T) {
                    x.keys[i] = deleteSuccessor(rightChild);
                } else {
                    mergeChildren(x, i);
                    delete(x.children[i], key);
                }
            }
        } else {
            if (x.isLeaf) {
                return;
            }

            Node child = x.children[i];

            if (child.n < T) {
                if (i > 0 && x.children[i - 1].n >= T) {
                    rotateRight(x, i);
                } else if (i < x.n && x.children[i + 1].n >= T) {
                    rotateLeft(x, i);
                } else {
                    if (i > 0) {
                        mergeChildren(x, i - 1);
                        i--;
                    } else {
                        mergeChildren(x, i);
                    }
                }
            }

            delete(x.children[i], key);
        }
    }

    private int deletePredecessor(Node x) {
        while (!x.isLeaf) {
            x = x.children[x.n];
        }
        int pred = x.keys[x.n - 1];
        x.n--;
        return pred;
    }

    private int deleteSuccessor(Node x) {
        while (!x.isLeaf) {
            x = x.children[0];
        }
        int succ = x.keys[0];
        for (int j = 0; j < x.n - 1; j++) {
            x.keys[j] = x.keys[j + 1];
        }
        x.n--;
        return succ;
    }

    private void rotateRight(Node x, int i) {
        Node leftChild = x.children[i - 1];
        Node rightChild = x.children[i];

        for (int j = rightChild.n; j > 0; j--) {
            rightChild.keys[j] = rightChild.keys[j - 1];
        }
        rightChild.keys[0] = x.keys[i - 1];

        if (!rightChild.isLeaf) {
            for (int j = rightChild.n + 1; j > 0; j--) {
                rightChild.children[j] = rightChild.children[j - 1];
            }
            rightChild.children[0] = leftChild.children[leftChild.n];
        }

        x.keys[i - 1] = leftChild.keys[leftChild.n - 1];
        rightChild.n++;
        leftChild.n--;
    }

    private void rotateLeft(Node x, int i) {
        Node leftChild = x.children[i];
        Node rightChild = x.children[i + 1];

        leftChild.keys[leftChild.n] = x.keys[i];

        if (!leftChild.isLeaf) {
            leftChild.children[leftChild.n + 1] = rightChild.children[0];
        }

        x.keys[i] = rightChild.keys[0];

        for (int j = 0; j < rightChild.n - 1; j++) {
            rightChild.keys[j] = rightChild.keys[j + 1];
        }

        if (!rightChild.isLeaf) {
            for (int j = 0; j < rightChild.n; j++) {
                rightChild.children[j] = rightChild.children[j + 1];
            }
        }

        rightChild.n--;
        leftChild.n++;
    }

    private void mergeChildren(Node x, int i) {
        Node leftChild = x.children[i];
        Node rightChild = x.children[i + 1];

        leftChild.keys[leftChild.n] = x.keys[i];

        for (int j = 0; j < rightChild.n; j++) {
            leftChild.keys[leftChild.n + 1 + j] = rightChild.keys[j];
        }

        if (!leftChild.isLeaf) {
            for (int j = 0; j <= rightChild.n; j++) {
                leftChild.children[leftChild.n + 1 + j] = rightChild.children[j];
            }
        }

        leftChild.n += rightChild.n + 1;

        for (int j = i + 1; j < x.n; j++) {
            x.keys[j - 1] = x.keys[j];
            x.children[j] = x.children[j + 1];
        }

        x.n--;
    }
}