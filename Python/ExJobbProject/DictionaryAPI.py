import json
import requests

app_id = "abbe1505"
app_key = "c1789e51f9e25323d694997c0a26d058"
endpoint = "entries"
language = "en-us"
word_id = ""
url = "https://od-api.oxforddictionaries.com/api/v2" + "/" + endpoint + "/" + language + "/" + word_id.lower()


def set_word(word):
    global word_id
    word_id = word.lower()

def get_synonyms(word):
    set_word(word)
    url = "https://od-api.oxforddictionaries.com/api/v2" + "/" + endpoint + "/" + language + "/" + word_id.lower()

    SynList = []
    try:
        r = requests.get(url, headers={"app_id": app_id, "app_key": app_key})
        data = json.loads(r.text)
        # Synonym finder
        synonymData = data["results"][0]["lexicalEntries"][0]["entries"][0]["senses"][0]["synonyms"]
        for i in range (len(synonymData)):
            SynList.append(synonymData[i]['text'])
    except:
        print("Error finding synonym")

    return SynList