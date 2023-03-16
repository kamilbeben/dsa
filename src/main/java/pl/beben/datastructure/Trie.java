package pl.beben.datastructure;

import lombok.RequiredArgsConstructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class Trie {

  final Node root = new Node(null);

  // delegating methods

  public void addWord(String word) {
    addWord(word.toCharArray());
  }

  public void removeWord(String word) {
    removeWord(word.toCharArray());
  }

  public boolean containsWord(String word) {
    final var node = findNode(word);
    return node != null && node.isEndOfWord;
  }

  public boolean contains(String string) {
    return findNode(string) != null;
  }

  private Node findNode(String string) {
    return findNode(string.toCharArray());
  }

  // actual logic

  // not really what you would do in a "real world" implementation, just wanted to implement it in two ways - recursively and iteratively

  public Set<String> recursivelySearchWordsStartingWith(String prefix) {
    final var words = new HashSet<String>();
    recursivelySearchWordsStartingWith(words, findNode(prefix), prefix);
    return words;
  }

  private void recursivelySearchWordsStartingWith(Set<String> words, Node node, String nodeValue) {

    if (node.isEndOfWord)
      words.add(nodeValue);

    node.charToNode
      .forEach(((childNodeCharacter, childNode) ->
        recursivelySearchWordsStartingWith(words, childNode, nodeValue + childNodeCharacter)
      ));
  }

  public Set<String> iterativelySearchWordsStartingWith(String prefix) {
    final var words = new HashSet<String>();

    final var nodeStack = new Stack<Node>();
    nodeStack.push(findNode(prefix.toCharArray()));

    final var nodeValueStack = new Stack<String>();
    nodeValueStack.push(prefix);

    while (!nodeStack.isEmpty()) {
      final var node = nodeStack.pop();
      final var nodeValue = nodeValueStack.pop();

      if (node.isEndOfWord)
        words.add(nodeValue);

      node.charToNode
        .forEach(((childNodeCharacter, childNode) -> {
          nodeValueStack.push(nodeValue + childNodeCharacter);
          nodeStack.push(childNode);
        }));
    }

    return words;
  }

  private void addWord(char[] word) {
    var node = root;
    for (var i = 0; i < word.length; i++) {
      final var character = word[i];
      final var characterIsEndOfWord = i == word.length - 1;

      if (!node.charToNode.containsKey(character))
        node.charToNode.put(character, new Node(node));

      node = node.charToNode.get(character);
      node.isEndOfWord |= characterIsEndOfWord;
    }
  }

  private void removeWord(char[] word) {

    var node = findNode(word);

    if (node.isEndOfWord)
      node.isEndOfWord = false;

    for (int i = word.length - 1; i >= 0; i--) {
      final var character = word[i];
      final var nodeIsRoot = node.parent == null;
      final var nodeHasChildren = !node.charToNode.isEmpty();

      if (nodeIsRoot || nodeHasChildren)
        return;

      assert node.parent.charToNode.get(character) == node;

      node.parent.charToNode.remove(character);
      node = node.parent;
    }
  }

  private Node findNode(char[] chars) {
    var node = root;
    for (var i = 0; node != null && i < chars.length; i++) {
      node = node.charToNode.get(chars[i]);
    }
    return node;
  }

  @RequiredArgsConstructor
  static class Node {
    final Node parent;
    final Map<Character, Node> charToNode = new HashMap<>();
    boolean isEndOfWord;

  }

}
