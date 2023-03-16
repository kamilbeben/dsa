package pl.beben.datastructure;

import com.google.common.collect.ImmutableSet;
import org.junit.Assert;
import org.junit.Test;

public class TrieTest {

  private Trie createTestTrie() {
    //
    //               ROOT
    //            c      d
    //         a            o
    //      r:    n            g:
    //   d:          c
    //                  e
    //                     r:
    //
    // legend:
    //   - any character - a node representing the following character
    //   - colon following any character (":") - end of word

    final var trie = new Trie();

    trie.addWord("car");
    trie.addWord("card");
    trie.addWord("cancer");
    trie.addWord("dog");

    return trie;
  }

  @Test
  public void addThenContains() {

    // given
    final var trie = createTestTrie();

    // then
    containsWord: {
      Assert.assertTrue(trie.containsWord("car"));
      Assert.assertTrue(trie.containsWord("card"));
      Assert.assertTrue(trie.containsWord("cancer"));
      Assert.assertTrue(trie.containsWord("dog"));

      Assert.assertFalse(trie.containsWord("ca"));
      Assert.assertFalse(trie.containsWord("can"));
      Assert.assertFalse(trie.containsWord("door"));
      Assert.assertFalse(trie.containsWord("dogs"));
    }

    // then
    contains: {
      Assert.assertTrue(trie.contains("ca"));
      Assert.assertTrue(trie.contains("car"));
      Assert.assertTrue(trie.contains("card"));
      Assert.assertTrue(trie.contains("can"));
      Assert.assertTrue(trie.contains("cancer"));
      Assert.assertTrue(trie.contains("do"));
      Assert.assertTrue(trie.contains("dog"));

      Assert.assertFalse(trie.contains("door"));
      Assert.assertFalse(trie.contains("cani"));
    }
  }

  @Test
  public void addThenRemove() {
    // given
    final var trie = createTestTrie();

    // when
    trie.removeWord("car");

    // then
    Assert.assertFalse(trie.containsWord("car"));

    Assert.assertTrue(trie.contains("car"));
    Assert.assertTrue(trie.containsWord("card"));
  }

  @Test
  public void addThenSearchWordsStartingWithPrefix() {
    // given
    final var trie = createTestTrie();
    final var expectedOutput = ImmutableSet.of("car", "card", "cancer");

    // then
    Assert.assertEquals(expectedOutput, trie.recursivelySearchWordsStartingWith("ca"));
    Assert.assertEquals(expectedOutput, trie.iterativelySearchWordsStartingWith("ca"));
  }

  @Test
  public void addThenSearchWordsStartingWithExactMatch() {
    // given
    final var trie = createTestTrie();
    final var expectedOutput = ImmutableSet.of("car", "card");

    // then
    Assert.assertEquals(expectedOutput, trie.recursivelySearchWordsStartingWith("car"));
    Assert.assertEquals(expectedOutput, trie.iterativelySearchWordsStartingWith("car"));
  }

  @Test
  public void addRemoveThenTestStructure() {

    // given
    final var trie = createTestTrie();

    // then
    Assert.assertNotNull(trie.root.charToNode.get('d'));

    Assert.assertNotNull(trie.root.charToNode.get('c'));
    Assert.assertNotNull(trie.root.charToNode.get('c').charToNode.get('a'));
    Assert.assertNotNull(trie.root.charToNode.get('c').charToNode.get('a').charToNode.get('r'));
    Assert.assertTrue(trie.root.charToNode.get('c').charToNode.get('a').charToNode.get('r').isEndOfWord);

    // when
    trie.removeWord("car");

    // then
    Assert.assertNotNull(trie.root.charToNode.get('c').charToNode.get('a').charToNode.get('r'));
    Assert.assertFalse(trie.root.charToNode.get('c').charToNode.get('a').charToNode.get('r').isEndOfWord);

    Assert.assertNotNull(trie.root.charToNode.get('c').charToNode.get('a').charToNode.get('r').charToNode.get('d'));
    Assert.assertTrue(trie.root.charToNode.get('c').charToNode.get('a').charToNode.get('r').charToNode.get('d').isEndOfWord);

    // when
    trie.removeWord("card");

    // then
    Assert.assertNotNull(trie.root.charToNode.get('c').charToNode.get('a'));
    Assert.assertNull(trie.root.charToNode.get('c').charToNode.get('a').charToNode.get('r'));
  }

}