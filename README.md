# First Generation Spell Checker

## Overview
This Java program implements a first-generation spell checker using Levenshtein Distance and dynamic programming. It reads a large dictionary and checks a full paragraph of input for spelling errors, suggesting the closest matching word.

## Files
- `Levenshtein.java`: Contains the edit distance algorithm.
- `SpellChecker.java`: Loads the dictionary, reads input, checks words, and suggests corrections.
- `dictionary.txt`: A small sample word list (you can replace this with a larger list like dwyl/english-words).

## How to Run
1. Compile the code:
   ```bash
   javac Levenshtein.java SpellChecker.java
   ```
2. Run the program:
   ```bash
   java SpellChecker
   ```
3. Paste a full paragraph and press ENTER + CTRL+D (or CMD+D on Mac) to finish input.

## Example Input
```
The quik brown fox jumpd over the lazi dog.
```

## Example Output
```
'quik' is incorrect. Did you mean: 'quick'?
'brown' is correct.
'fox' is correct.
'jumpd' is incorrect. Did you mean: 'jumped'?
'over' is correct.
'the' is correct.
'lazi' is incorrect. Did you mean: 'lazy'?
'dog' is correct.
```

## Analysis Notes
- Brute-force search: checks every dictionary word, slow for large dictionaries.
- Levenshtein Distance: dynamic programming approach.
- Possible improvements: top-3 suggestions, word frequency weighting, optimized search structures.