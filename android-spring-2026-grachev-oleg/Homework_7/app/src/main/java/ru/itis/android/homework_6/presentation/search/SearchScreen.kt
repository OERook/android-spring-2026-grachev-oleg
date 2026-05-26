package ru.itis.android.homework_6.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import ru.itis.android.homework_6.R
import ru.itis.android.homework_6.domain.model.Character

@Composable
fun SearchScreen(
    onCharacterClick: (Int) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val query by viewModel.searchQuery.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    val successState = uiState as? SearchUiState.Success
    val shouldShowSnackbar = successState != null &&
        successState.characters.isNotEmpty() &&
        !successState.isMessageConsumed
    val isFromCache = successState?.isFromCache == true

    LaunchedEffect(shouldShowSnackbar, isFromCache) {
        if (shouldShowSnackbar) {
            val message = if (isFromCache) {
                context.getString(R.string.data_loaded_from_cache)
            } else {
                context.getString(R.string.data_loaded_from_server)
            }
            snackbarHostState.showSnackbar(message)
            viewModel.consumeMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            UserIdLabel(userIdShort = viewModel.currentUserId.take(8))
            Spacer(modifier = Modifier.height(8.dp))

            SearchInput(
                query = query,
                onQueryChange = viewModel::updateQuery,
                onSearch = {
                    keyboardController?.hide()
                    viewModel.performSearch()
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            SearchButton(
                onClick = {
                    keyboardController?.hide()
                    viewModel.performSearch()
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            CrashButton(query = query, userIdShort = viewModel.currentUserId.take(8))

            Spacer(modifier = Modifier.height(16.dp))

            SearchContent(state = uiState, onCharacterClick = onCharacterClick)
        }
    }
}

@Composable
private fun UserIdLabel(userIdShort: String) {
    Text(
        text = "User: $userIdShort",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun SearchInput(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        label = { Text(stringResource(R.string.enter_character_name)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() })
    )
}

@Composable
private fun SearchButton(onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text(stringResource(R.string.search_button))
    }
}

@Composable
private fun CrashButton(query: String, userIdShort: String) {
    OutlinedButton(
        onClick = {
            com.google.firebase.crashlytics.FirebaseCrashlytics.getInstance().apply {
                log("User pressed Test crash button")
                setCustomKey("last_query", query)
            }
            throw RuntimeException("Test crash from Homework_7 — user=$userIdShort")
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Test crash (Crashlytics)")
    }
}

@Composable
private fun SearchContent(
    state: SearchUiState,
    onCharacterClick: (Int) -> Unit
) {
    when (state) {
        is SearchUiState.Idle -> CenteredMessage(
            text = stringResource(R.string.start_searching),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        is SearchUiState.Loading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }
        is SearchUiState.Error -> CenteredMessage(
            text = state.message,
            color = MaterialTheme.colorScheme.error
        )
        is SearchUiState.Success -> CharacterList(
            characters = state.characters,
            onCharacterClick = onCharacterClick
        )
    }
}

@Composable
private fun CenteredMessage(text: String, color: androidx.compose.ui.graphics.Color) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = text, color = color)
    }
}

@Composable
private fun CharacterList(
    characters: List<Character>,
    onCharacterClick: (Int) -> Unit
) {
    if (characters.isEmpty()) {
        CenteredMessage(
            text = stringResource(R.string.nothing_found),
            color = MaterialTheme.colorScheme.onSurface
        )
        return
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(characters, key = { it.id }) { character ->
            CharacterItem(character = character, onClick = onCharacterClick)
        }
    }
}

@Composable
fun CharacterItem(character: Character, onClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(character.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = character.imageUrl,
                contentDescription = character.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${character.species} • ${character.status}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
