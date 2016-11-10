const React = require('react');
const ReactDOM = require('react-dom');

class App extends React.Component {

	constructor(props) {
		super(props);
		this.state = {events: []};
	}

	componentDidMount() {
		fetch(`/messages`)
			.then(response => {
				return response.text();
			})
			.then(text => {
				this.setState({events: text.split("\n")});
			});
	}

	render() {
		return (
			<p class="form">
				<input class="form__input" name="message" />
				<EntryList events={this.state.events}/>
			</p>
		)
	}
}

class EntryList extends React.Component{
	render() {
		var events = this.props.events.map(event =>
			<Entry key={event.split("\t")[0]} time={event.split("\t")[1]} icon={event.split("\t")[2]} message={event.split("\t")[3]}/>
		);
		return (
			<table class="eventlist">
				<tbody>
					{events}
				</tbody>
			</table>
		)
	}
}

class Entry extends React.Component{
	render() {
		return (
			<tr class="elementlist__element" data-id="{this.props.key}">
				<td>{new Date(this.props.time * 1000).toISOString()}</td>
				<td>{this.props.icon}</td>
				<td>{this.props.message}</td>
			</tr>
		)
	}
}

ReactDOM.render(
	<App />,
	document.getElementById('react')
)